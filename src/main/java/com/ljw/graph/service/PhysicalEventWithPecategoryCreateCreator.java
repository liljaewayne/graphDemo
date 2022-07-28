package com.ljw.graph.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

    /**
     * @return 所有到达target的路径, 如果没有能到达target的路径, 则返回空列表
     */
    public static List<Road> findShortestRoadsInGraph(List<String> beginWords, String target) {
        List<Road> shortestRoads = new ArrayList<>();

        for (String beginWord : beginWords) {
            List<Road> shortestRoadsOfCurBeginWord = findShortestRoadsInGraph(beginWord, target);

            if (shortestRoadsOfCurBeginWord.size() > 0) {
                if (shortestRoads.size() == 0) {
                    shortestRoads.addAll(shortestRoadsOfCurBeginWord);
                } else {
                    if (shortestRoadsOfCurBeginWord.get(0).getSteps().size() < shortestRoads.get(0).getSteps().size()) {
                        shortestRoads.clear();
                        shortestRoads.addAll(shortestRoadsOfCurBeginWord);
                    } else if (shortestRoadsOfCurBeginWord.get(0).getSteps().size() == shortestRoads.get(0).getSteps().size()) {
                        shortestRoads.addAll(shortestRoadsOfCurBeginWord);
                    }// else doesNothing

                }


            }
        }

        return shortestRoads;
    }

    /**
     * @return 所有到达target的路径
     * 如果没有能到达target的路径, 则返回null
     * 如果起始点就是目标点, 则返回空列表
     * 如果起始点不是目标点, 则返回非空列表
     */
    public static List<Road> findShortestRoadsInGraph(String beginWord, String target) {
        return ShortestBfsProcessor.findShortestRoadsInGraph(beginWord, target);
    }

    /**
     * @return 所有到达target的路径
     * 如果没有能到达target的路径, 则返回null
     * 如果起始点就是目标点, 则返回空列表
     * 如果起始点不是目标点, 则返回非空列表
     * (没watch是null, 则没试过, watch了是null, 则无法到达)
     * @deprecated 用ShortestBfsProcessor.findShortestRoadsInGraph(beginWord, target);代替
     */
    public static List<Road> findRoads(Road beginRoad, String target, Map<String, Set<Road>> shortestRoadsDic, Set<String> watched) {

        watched.add(beginRoad.getBeginWord());

        if (beginRoad.getBeginWord().equals(target)) {
            shortestRoadsDic.put(beginRoad.getBeginWord(), new HashSet<>());
            return new ArrayList<>(shortestRoadsDic.get(beginRoad.getBeginWord()));
        }

        HashMap<String, Set<Step>> neighbors = g.get(beginRoad.getBeginWord());

        List<Road> beginToTargetShortestRoads = null;

        for (Map.Entry<String, Set<Step>> entry : neighbors.entrySet()) {

            String neighborWord = entry.getKey();
            Set<Step> steps = entry.getValue();

            Set<Road> neighborToTargetShortestRoads = shortestRoadsDic.get(neighborWord);

            if (neighborToTargetShortestRoads == null && !watched.contains(neighborWord)) {// 未找的找
                List<Road> roadsNeighborWordToTarget = findRoads(
                        new Road(neighborWord),
                        target,
                        shortestRoadsDic,
                        watched
                );

                if (roadsNeighborWordToTarget == null) {// neighborToTarget不通
                    shortestRoadsDic.put(neighborWord, null);
                } else {// 通
                    neighborToTargetShortestRoads = new HashSet<>(roadsNeighborWordToTarget);
                    shortestRoadsDic.put(neighborWord, new HashSet<>(neighborToTargetShortestRoads));
                }

            }


            // 找过的, 取路径判断
            if (neighborToTargetShortestRoads == null) {// neighborToTarget试过不通, 则看其他邻居的路径
                continue;
            }

            if (neighborToTargetShortestRoads.size() == 0) {// neighbor就是Target, 则beginWordToTarget的最短路径就是beginWordToNeighbor, 整体路径也要比对加上取当前neighbor就是最短
                Set<Step> beginToNeighborSteps = TripleGraphUtil.stepOf(beginRoad.getBeginWord(), neighborWord);
                List<Road> beginToNeighborRoad_同时也是_beginToTargetShortestRoad_群 = Road.gen1StepRoads_beginToSameEndSteps(beginRoad.getBeginWord(), beginToNeighborSteps);

                beginToTargetShortestRoads = 取最短路径群(beginToTargetShortestRoads, Sets.newHashSet(beginToNeighborRoad_同时也是_beginToTargetShortestRoad_群));

                shortestRoadsDic.put(beginRoad.getBeginWord(), new HashSet<>(beginToTargetShortestRoads));
                return beginToTargetShortestRoads;
            }

            // neighbor不是Target, 但有路径可以到达Target, 其最短路径就是 neighborToTargetShortestRoads, beginToNeighborToTarget的最短路径就是先一步去Neighbor, 然后沿neighborToTargetShortestRoads走
            Set<Road> beginToNeighborToTargetShortestRoads = new HashSet<>();
            for (Road neighborToTargetShortestRoad : neighborToTargetShortestRoads) {

                Set<Step> beginToNeighborSteps = TripleGraphUtil.stepOf(beginRoad.getBeginWord(), neighborWord);

                List<Road> beginToNeighborRoad_群_assert1Step = Road.gen1StepRoads_beginToSameEndSteps(beginRoad.getBeginWord(), beginToNeighborSteps);

                for (Road beginToNeighborRoad_assert1Step : beginToNeighborRoad_群_assert1Step) {
                    Road beginToNeighborToTargetShortestRoad = RoadUtil.roadToMidToTail(beginToNeighborRoad_assert1Step, neighborToTargetShortestRoad);
                    beginToNeighborToTargetShortestRoads.add(beginToNeighborToTargetShortestRoad);
                }
            }

            beginToTargetShortestRoads = 取最短路径群(beginToTargetShortestRoads, beginToNeighborToTargetShortestRoads);

        }

        // 整体找完了, 如果无法到达target, 则dic中放入null, 如果可以到达target, dic中放入最短路径群
        if (beginToTargetShortestRoads == null) {
            shortestRoadsDic.put(
                    beginRoad.getBeginWord(),
                    null
            );
        } else {
            shortestRoadsDic.put(
                    beginRoad.getBeginWord(),
                    new HashSet<>(beginToTargetShortestRoads)
            );
        }


        return beginToTargetShortestRoads;
    }

    /**
     * @param beginToTargetShortestRoads
     * @param beginToNeighborToTargetShortestRoads
     * @return
     */
    private static List<Road> 取最短路径群(List<Road> beginToTargetShortestRoads, Set<Road> beginToNeighborToTargetShortestRoads) {
        if (beginToTargetShortestRoads == null) {// beginToTarget整体路径还没找到, 就先取当前的路径
            beginToTargetShortestRoads = new ArrayList<>(beginToNeighborToTargetShortestRoads);
        } else {// beginToTarget整体路径找到过, 就比较之前找到的 和 现在找到的经过该neighbor的最短路径比较, 取最短的路径作为整体路径

            if (
                    beginToTargetShortestRoads.get(0).getSteps().size() < beginToNeighborToTargetShortestRoads.iterator().next().getSteps().size()
            ) {// 之前的短, 取之前的

            } else if (
                    beginToTargetShortestRoads.get(0).getSteps().size() == beginToNeighborToTargetShortestRoads.iterator().next().getSteps().size()
            ) {// 一样长, 把beginToNeighborToTarget的路径也加上
                beginToTargetShortestRoads.addAll(beginToNeighborToTargetShortestRoads);
            } else if (
                    beginToTargetShortestRoads.get(0).getSteps().size() > beginToNeighborToTargetShortestRoads.iterator().next().getSteps().size()
            ) {// beginToNeighborToTarget路径更短, 之前的清空, 也取beginToNeighborToTarget的所有路径
                beginToTargetShortestRoads.clear();
                beginToTargetShortestRoads.addAll(beginToNeighborToTargetShortestRoads);
            }

        }
        return beginToTargetShortestRoads;
    }


    public static List<NodeWithModifiers> createActToTarget的完整路径Nms(String beginAct, String target) {
        List<String> actionReceiverNames = PecUtil.findActionReceiverNames(beginAct);
        if (actionReceiverNames.contains(target)) {
            return Lists.newArrayList(
                    new NodeWithModifiers(Node.of(target))
            );
        }

        List<Road> roads = findShortestRoadsInGraph(actionReceiverNames, target);

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

        List<Road> roads = findShortestRoadsInGraph(actionSenderNames, target);

        List<NodeWithModifiers> nms = RoadUtil.buildNmListByRoads(roads);

        return nms;
    }
}
