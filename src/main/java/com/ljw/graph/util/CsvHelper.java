package com.ljw.graph.util;

import com.ljw.graph.entity.PhysicalEventWithPecategoryCreate;

import java.util.List;
import java.util.StringJoiner;

public class CsvHelper {

    public static String toCsvString(List<PhysicalEventWithPecategoryCreate> peccs) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (PhysicalEventWithPecategoryCreate pecc : peccs) {
            String s = pecc.toCsvString();
            stringJoiner.add(s);
        }
        return stringJoiner.toString();
    }
}
