package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * relation_node
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationNode implements Serializable, Node {
    private Integer id;

    private String name;

    private String relation;

    private static final long serialVersionUID = 1L;

    public RelationNode(String name, String relation) {
        this.name = name;
        this.relation = relation;
    }

    // 3.1.4 relation节点对应的类节点
    public String getPeCategoryStr() {
        // e_prepa和e_prepc类型的relation节点对应的类节点是c_prep
        if (
                StringUtils.equals("e_prepa", relation) ||
                        StringUtils.equals("e_prepc", relation)
        ) {
            return "c_prep";
        } else {
            return RegExUtils.replaceFirst(relation, "e_", "c_");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationNode that = (RelationNode) o;
        return Objects.equals(name, that.name) && Objects.equals(relation, that.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relation);
    }

    @Override
    public String getType() {
        return "RelationNode";
    }

}