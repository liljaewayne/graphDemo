package com.ljw.graph.service;

import com.ljw.graph.entity.graph.Step;
import com.ljw.graph.util.PecUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private static Map<String, HashMap<String, Set<Step>>> g;

    static {
        g = PecUtil.graphOfTriples();
    }


    public static Set<String> adj(String v) {
        return g.get(v).keySet();
    }


    public static Set<Step> stepOf(String beginV, String toV) {
        if (!haveEdge(beginV, toV)) {
            return new HashSet<>();
        }
        HashMap<String, Set<Step>> neighbors = g.get(beginV);
        Set<Step> steps = neighbors.get(toV);
        return steps;
    }


    public static boolean haveEdge(String v, String w) {

        HashMap<String, Set<Step>> neighbors = g.get(v);

        return neighbors.containsKey(w);

    }

}
