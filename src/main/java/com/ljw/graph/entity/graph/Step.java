package com.ljw.graph.entity.graph;


import lombok.Data;

import java.util.Objects;

@Data
public class Step {
    private String word;
    private String edge;
    private Place place;

    public Step(String word, String edge, Place place) {
        this.word = word;
        this.edge = edge;
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return Objects.equals(word, step.word) && Objects.equals(edge, step.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, edge);
    }
}
