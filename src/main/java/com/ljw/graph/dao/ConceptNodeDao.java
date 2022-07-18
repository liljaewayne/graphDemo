package com.ljw.graph.dao;

import com.ljw.graph.entity.ConceptNode;
import com.ljw.graph.entity.KgTriple;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConceptNodeDao {

    @SneakyThrows
    public List<ConceptNode> selectAll() {
        File file = new File("input/ConceptNode.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> strings = reader.lines().skip(1).collect(Collectors.toList());


        List<ConceptNode> res = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split(",");

            ConceptNode conceptNode = new ConceptNode(split[0], split[1]);
            res.add(conceptNode);
        }

        return res;
    }

}