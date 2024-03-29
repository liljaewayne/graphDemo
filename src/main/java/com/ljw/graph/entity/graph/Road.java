package com.ljw.graph.entity.graph;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
@EqualsAndHashCode
public class Road {
    private String beginWord;
    private List<Step> steps = new ArrayList<>();

    public Road(String beginWord) {
        this.beginWord = beginWord;
        this.steps = new ArrayList<>();
    }

    public Road(String beginWord, List<Step> steps) {
        this.beginWord = beginWord;
        this.steps = steps;
    }

    public String endWord() {
        if (steps.size() == 0) {
            return null;
        }

        return steps.get(steps.size() - 1).getWord();
    }


    public void addStep(Step step) {
        steps.add(step);
    }


    public static Road connect(Road front, Road rare) {
        Road connectedRoad = new Road(front.getBeginWord(), new ArrayList<>());

        assert front.getSteps().size() > 0;
        assert rare.getSteps().size() > 0;

        Step frontLastStep = front.getSteps().get(front.getSteps().size() - 1);
        assert frontLastStep.getWord().equals(rare.getBeginWord());

        for (Step step : front.getSteps()) {
            connectedRoad.addStep(step);
        }
        for (Step step : rare.getSteps()) {
            connectedRoad.addStep(step);
        }

        return connectedRoad;
    }

    public static List<Road> gen1StepRoads_beginToSameEndSteps(String beginWord, Set<Step> sameEndSteps) {
        List<Road> roads = new ArrayList<>();
        for (Step step : sameEndSteps) {
            roads.add(
                    new Road(beginWord, Lists.newArrayList(step))
            );
        }
        return roads;
    }

}
