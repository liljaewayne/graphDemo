package com.ljw.graph.service;

import com.google.common.collect.Lists;
import com.ljw.graph.entity.graph.Road;
import com.ljw.graph.entity.graph.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class ShortestBfsProcessor {

    /**
     * @return 所有到达target的路径
     * 如果没有能到达target的路径, 则返回null
     * 如果起始点就是目标点, 则返回空列表
     * 如果起始点不是目标点, 则返回非空列表
     */
    public static List<Road> findShortestRoadsInGraph(String beginWord, String target) {

        Set<String> visited = new HashSet<>();
        Map<String, String> from = new HashMap<>();
        Map<String, Integer> order = new HashMap<>();

        findBfs(beginWord, visited, from, order);


        if (order.get(target) == null) {// 无路
            return null;
        }

        // 有路
        Integer totalLen = order.get(target);
        if (totalLen == 0) {
            return new ArrayList<>();// 自己到自己
        }

        List<String>[] lenVertexesArr = new List[totalLen + 1];
        for (int i = 0; i < lenVertexesArr.length; i++) {
            lenVertexesArr[i] = new ArrayList<>();
        }
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String v = entry.getKey();
            Integer lenToBegin = entry.getValue();
            if (lenToBegin <= totalLen) {
                lenVertexesArr[lenToBegin].add(v);
            }
        }

        // 先铺路1步, 然后从2步开始遍历dp
        List<Road>[] lenRoadsArr = new List[totalLen + 1];
        lenRoadsArr[0] = new ArrayList<>();

        lenRoadsArr[1] = new ArrayList<>();
        List<String> oneStepWords = lenVertexesArr[1];
        for (String oneStepWord : oneStepWords) {
            Set<Step> steps = Graph.stepOf(beginWord, oneStepWord);
//            assert steps.size() > 0;
            List<Road> oneStepRoads = new ArrayList<>();
            for (Step step : steps) {
                Road oneStepRoad = new Road(beginWord, Lists.newArrayList(step));
                oneStepRoads.add(oneStepRoad);
            }
            lenRoadsArr[1].addAll(oneStepRoads);
        }

        if (totalLen == 1) {
            List<Road> roads1 = lenRoadsArr[1];
            roads1 = filterByEndWord(roads1, target);
            return roads1;// 一步的结果
        }


        for (int i = 2; i < lenRoadsArr.length; i++) {

            List<Road> beginToMidRoads = lenRoadsArr[i - 1];
            List<String> ends = lenVertexesArr[i];

            List<Road> beginToEndRoads = new ArrayList<>();

            for (Road beginToMidRoad : beginToMidRoads) {
                String beginV = beginToMidRoad.endWord();
                for (String endV : ends) {
                    if (!Graph.haveEdge(beginV, endV)) {// 无路, 忽略
                        continue;
                    }
                    // 有边, 算路

                    Set<Step> steps = Graph.stepOf(beginV, endV);// 最后一步

                    for (Step step : steps) {
                        Road beginToEndRoad = Road.connect(
                                beginToMidRoad,
                                new Road(beginV, Lists.newArrayList(step))
                        );
                        beginToEndRoads.add(beginToEndRoad);
                    }


                }

            }

            lenRoadsArr[i] = beginToEndRoads;

        }

        List<Road> roadsN = lenRoadsArr[totalLen];
        roadsN = filterByEndWord(roadsN, target);

        return roadsN;
    }

    private static void findBfs(String beginVertex, Set<String> visited, Map<String, String> from, Map<String, Integer> order) {
        Queue<String> queue = new LinkedList<>();

        queue.add(beginVertex);
        visited.add(beginVertex);
        order.put(beginVertex, 0);

        while (!queue.isEmpty()) {
            String v = queue.remove();
            Set<String> adj = Graph.adj(v);

            for (String i : adj) {

                if (!visited.contains(i)) {
                    queue.add(i);
                    visited.add(i);
                    from.put(i, v);
                    order.put(
                            i,
                            order.get(v) + 1
                    );
                }

            }


        }


    }

    private static List<Road> filterByEndWord(List<Road> roads, String target) {
        List<Road> res = new ArrayList<>();
        for (Road road : roads) {
            if (StringUtils.equals(road.endWord(), target)) {
                res.add(road);
            }
        }
        return res;
    }


}
