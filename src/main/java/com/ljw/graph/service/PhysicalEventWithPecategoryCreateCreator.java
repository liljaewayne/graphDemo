package com.ljw.graph.service;

import com.google.common.collect.Lists;
import com.ljw.graph.entity.Node;
import com.ljw.graph.entity.NodeWithModifiers;
import com.ljw.graph.entity.graph.Road;
import com.ljw.graph.entity.graph.Step;
import com.ljw.graph.util.PecUtil;
import com.ljw.graph.util.RoadUtil;
import com.ljw.graph.util.TripleGraphUtil;

import java.util.*;

public class PhysicalEventWithPecategoryCreateCreator {
    // 建图
    private static Map<String, HashMap<String, Set<Step>>> g = PecUtil.graphOfTriples();

    public static List<NodeWithModifiers> createActToTarget的完整路径Nms(String beginAct, String target) {
        List<String> actionReceiverNames = PecUtil.findActionReceiverNames(beginAct);
        if (actionReceiverNames.contains(target)) {
            return Lists.newArrayList(
                    new NodeWithModifiers(Node.of(target))
            );
        }

        List<Road> roads = findRoadsInGraph(actionReceiverNames, target);

        List<NodeWithModifiers> nms = RoadUtil.buildNmListByRoads(roads);

        return nms;
    }

    public static List<NodeWithModifiers> createTargetToAct的完整路径Nms(String beginAct, String target) {
        List<String> actionSenderNames = PecUtil.findActionSenderNames(beginAct);
        if (actionSenderNames.contains(target)) {
            return Lists.newArrayList(
                    new NodeWithModifiers(Node.of(target))
            );
        }

        List<Road> roads = findRoadsInGraph(actionSenderNames, target);

        List<NodeWithModifiers> nms = RoadUtil.buildNmListByRoads(roads);

        return nms;

//        return new ArrayList<>();
    }


    /**
     * @return 所有到达target的路径, 如果没有能到达target的路径, 则返回空列表
     */
    private static List<Road> findRoadsInGraph(List<String> beginWords, String target) {
        List<Road> roads = new ArrayList<>();

        for (String beginWord : beginWords) {
            Road initialRoad = new Road(beginWord);

            Map<String, Set<Road>> shortestRoadsDic = new HashMap<>();
//            shortestRoadsDic.putAll(TripleGraphUtil.neighbors(target));// 不能初始化这个, 因为是终点不是起点


            List<Road> roadsOfCurBeginWord = findRoads(initialRoad, target, shortestRoadsDic, 1);

            if (roadsOfCurBeginWord.size() > 0) {
                roads.addAll(roadsOfCurBeginWord);
            }
        }

        return roads;
    }


    private static final int MAX_LEVEL = 10;

    private static List<Road> findRoads(Road beginRoad, String target, Map<String, Set<Road>> shortestRoadsDic, int level) {

        if (level > MAX_LEVEL || !g.containsKey(beginRoad.getBeginWord())) {
            return new ArrayList<>();
        }

        if (TripleGraphUtil.connected(beginRoad.getBeginWord(), target)) {
            // 返回此类路径(就是一批最短路径了)
            List<Road> shortestRoads = Road.of(beginRoad.getBeginWord(), TripleGraphUtil.stepOf(beginRoad.getBeginWord(), target));
            shortestRoadsDic.put(beginRoad.getBeginWord(), new HashSet<>(shortestRoads));// FIXME: 函数返回值统一为set
            return shortestRoads;
        }

        HashMap<String, Set<Step>> neighbors = g.get(beginRoad.getBeginWord());

        List<Road> beginToTargetShortestRoads = new ArrayList<>();

        for (Map.Entry<String, Set<Step>> entry : neighbors.entrySet()) {

            String neighborWord = entry.getKey();
            Set<Step> steps = entry.getValue();

            // begin到target经过该neighbor的最短roads

            if (!shortestRoadsDic.containsKey(neighborWord)) {
                List<Road> roadsNeighborWordToTarget = findRoads(
                        new Road(neighborWord),
                        target,
                        shortestRoadsDic,
                        level + 1
                );
                if (roadsNeighborWordToTarget.size() > 0) {
                    shortestRoadsDic.put(neighborWord, new HashSet<>(roadsNeighborWordToTarget));
                }
            }

            Set<Road> neighborToTargetRoads = shortestRoadsDic.get(neighborWord);
            if (neighborToTargetRoads == null || neighborToTargetRoads.size() == 0) {// 没有路径, 从neighbor到不了target
                continue;
            }

            List<Road> beginWordToNeighborRoads = new ArrayList<>();
            for (Step step : steps) {
                Road beginWordToNeighborRoad = new Road(beginRoad.getBeginWord(), Lists.newArrayList(step));
                beginWordToNeighborRoads.add(beginWordToNeighborRoad);
            }

            List<Road> beginToNeighborAndTargetShortestRoads = new ArrayList<>();
            for (Road neighborToTargetRoad : neighborToTargetRoads) {
                for (Road beginWordToNeighborRoad : beginWordToNeighborRoads) {
                    Road beginToTargetRoad = Road.connect(beginWordToNeighborRoad, neighborToTargetRoad);
                    beginToNeighborAndTargetShortestRoads.add(beginToTargetRoad);
                }
            }

            // 规划
            if (beginToTargetShortestRoads.size() == 0) {
                beginToTargetShortestRoads = new ArrayList<>(beginToNeighborAndTargetShortestRoads);
            } else {
                int oldStepNum = beginToTargetShortestRoads.get(0).getSteps().size();
                if (oldStepNum > beginToNeighborAndTargetShortestRoads.get(0).getSteps().size()) {// 如果经过neighborWord去target的路径比之前的路径短, 则更新为'经过neighborWord'的路径
                    beginToTargetShortestRoads = new ArrayList<>(beginToNeighborAndTargetShortestRoads);
                }// else, 保留原来路径

            }

        }


        shortestRoadsDic.put(
                beginRoad.getBeginWord(),
                new HashSet<>(beginToTargetShortestRoads)
        );

        return beginToTargetShortestRoads;
    }


}
