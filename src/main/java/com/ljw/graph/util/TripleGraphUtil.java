package com.ljw.graph.util;

import com.ljw.graph.entity.graph.Road;
import com.ljw.graph.entity.graph.Step;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TripleGraphUtil {

    private static Map<String, HashMap<String, Set<Step>>> g = PecUtil.graphOfTriples();

    public static boolean connected(String word1, String word2) {
        HashMap<String, Set<Step>> neighbors = g.get(word1);
        return neighbors.containsKey(word2);
    }

    public static Set<Step> stepOf(String word1, String word2) {
        if (connected(word1, word2)) {
            HashMap<String, Set<Step>> neighbors = g.get(word1);
            return neighbors.get(word2);
        }
        return new HashSet<>();
    }


    public static Map<String, Set<Road>> neighbors(String word) {
        HashMap<String, Set<Step>> neighborsOfStepsDic = g.get(word);
        HashMap<String, Set<Road>> neighborsOfRoadsDic = new HashMap<>();
        for (Map.Entry<String, Set<Step>> entry : neighborsOfStepsDic.entrySet()) {
            String neighborWord = entry.getKey();
            Set<Step> neighborSteps = entry.getValue();
            Set<Road> neighborRoads = RoadUtil.stepsToRoads(neighborSteps, word);
            neighborsOfRoadsDic.put(neighborWord, neighborRoads);
        }

        return neighborsOfRoadsDic;
    }
}
