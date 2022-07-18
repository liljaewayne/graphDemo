package com.ljw.graph.dao;


import com.ljw.graph.entity.PecategoryCreate;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PecategoryCreateDao {

    @SneakyThrows
    public List<PecategoryCreate> selectAll() {
        File file = new File("input/PecategoryCreate.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> strings = reader.lines().skip(1).collect(Collectors.toList());


        List<PecategoryCreate> res = new ArrayList<>();
        for (String s : strings) {

            String[] split = s.split(",");
            PecategoryCreate pecategoryCreate = new PecategoryCreate(
                    split[0],
                    split[1],
                    split[2],
                    split[3]
            );

            res.add(pecategoryCreate);
        }

        return res;
    }
}