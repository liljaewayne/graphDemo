package com.ljw.graph.dao;

import com.ljw.graph.entity.KgTriple;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class KgTripleDao {

    @SneakyThrows
    public List<KgTriple> selectAll() {
        File file = new File("input/KgTriples.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> strings = reader.lines().skip(1).collect(Collectors.toList());


        List<KgTriple> res = new ArrayList<>();
        for (String s : strings) {

            KgTriple kgTriple = KgTriple.of(s);
            res.add(kgTriple);
        }

        return res;
    }

}