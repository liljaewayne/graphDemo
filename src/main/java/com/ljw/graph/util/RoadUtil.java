package com.ljw.graph.util;

import com.google.common.collect.Lists;
import com.ljw.graph.entity.Modifier;
import com.ljw.graph.entity.Node;
import com.ljw.graph.entity.NodeWithModifiers;
import com.ljw.graph.entity.graph.Place;
import com.ljw.graph.entity.graph.Road;
import com.ljw.graph.entity.graph.Step;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoadUtil {
    public static List<NodeWithModifiers> buildNmListByRoads(List<Road> roads) {
        List<NodeWithModifiers> res = new ArrayList<>();

        for (Road road : roads) {
            String beginWord = road.getBeginWord();

            NodeWithModifiers beginNm = new NodeWithModifiers(Node.of(beginWord));
            NodeWithModifiers outNm = beginNm;

            List<Step> steps = road.getSteps();
            for (int i = 0; i < steps.size(); i++) {
                Step step = steps.get(i);

                String word = step.getWord();
                String act = step.getEdge();
                Place place = step.getPlace();

                Modifier modifier;

                if (place == Place.LEFT) {
                    modifier = new Modifier(
                            new NodeWithModifiers(Node.of(word)),
                            new NodeWithModifiers(Node.of(act)),
                            new NodeWithModifiers(Node.of(""))
                    );
                    outNm.setModifiers(Lists.newArrayList(modifier));
                    outNm = outNm.getModifiers().get(0).getNm1();
                } else {
                    modifier = new Modifier(
                            new NodeWithModifiers(Node.of("")),
                            new NodeWithModifiers(Node.of(act)),
                            new NodeWithModifiers(Node.of(word))
                    );
                    outNm.setModifiers(Lists.newArrayList(modifier));
                    outNm = outNm.getModifiers().get(0).getNm3();
                }


            }

            res.add(beginNm);


        }


        return res;
    }

    /**
     * @param roads 同样起点与同样终点的路径
     * @return 同一个起点到同一个终点的所有路径中最短的路径集合
     */
    public static List<Road> shortestRoadsOfSameBeginAndSameEnd(List<Road> roads) {
        List<Road> res = new ArrayList<>();
        int curSteps = Integer.MAX_VALUE;

        for (Road road : roads) {
            if (road.getSteps().size() < curSteps) {
                res.clear();
                res.add(road);
            } else if (road.getSteps().size() == curSteps) {
                res.add(road);
            }// else {} // road.getSteps().size() > curSteps  不add

        }


        return res;
    }

    public static Set<Road> stepsToRoads(Set<Step> neighborSteps, String beginWord) {
        Set<Road> roads = new HashSet<>();
        for (Step step : neighborSteps) {
            roads.add(
                    new Road(beginWord, Lists.newArrayList(step))
            );
        }
        return roads;
    }
}
