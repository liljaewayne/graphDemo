package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * concept_node
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptNode implements Serializable, Node {
    private Integer id;

    private String name;

    private String conceptType;

    private static final long serialVersionUID = 1L;

    public ConceptNode(String name, String conceptType) {
        this.name = name;
        this.conceptType = conceptType;
    }

    public ConceptNode(String name) {
        this(null, name, "NONE");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptNode that = (ConceptNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String getType() {
        return "ConceptNode";
    }


}