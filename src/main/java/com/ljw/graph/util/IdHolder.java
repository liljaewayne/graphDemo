package com.ljw.graph.util;

import java.util.HashMap;
import java.util.Map;

public class IdHolder {

    private static Map<String, Integer> map = new HashMap<>();

    public static int get(String pojoName) {
        if (!map.containsKey(pojoName)) {
            set(pojoName, 1);
        }

        Integer id = map.get(pojoName);
        map.put(pojoName, id + 1);

        return id;
    }

    public static void set(String pojoName, int initial) {
        map.put(pojoName, initial);
    }

}
