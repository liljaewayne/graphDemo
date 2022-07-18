package com.ljw.graph.dao;

import com.ljw.graph.entity.KgTriple;
import com.ljw.graph.entity.RelationNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelationNodeDao {


    @SneakyThrows
    public List<RelationNode> selectAll() {
        File file = new File("input/RelationNode.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> strings = reader.lines().skip(1).collect(Collectors.toList());


        List<RelationNode> res = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split(",");
            RelationNode relationNode = new RelationNode(split[0], split[1]);
            res.add(relationNode);
        }

        return res;
    }

}