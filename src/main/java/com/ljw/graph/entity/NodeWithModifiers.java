package com.ljw.graph.entity;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class NodeWithModifiers implements Serializable {
    private Node base;
    private List<Modifier> modifiers = new ArrayList<>();
    private Modifier query;// default null

    public void setQuery(Modifier query) {
        setQueryAndSetModifiersEmpty(query);
    }

    public void setQueryAndSetModifiersEmpty(Modifier query) {
        this.query = query;
        if (query != null) {
            modifiers = new ArrayList<>();
        }
    }

    public NodeWithModifiers(Node base, List<Modifier> modifiers) {
        this.base = base;
        this.modifiers = modifiers;
    }

    public NodeWithModifiers(Node base) {
        this.base = base;
    }

    public static boolean isEmpty(NodeWithModifiers nodeWithModifiers) {
        if (nodeWithModifiers == null) {
            return true;
        }
        if (nodeWithModifiers.getBase() == null) {
            return true;
        }
        if (StringUtils.isBlank(nodeWithModifiers.getBase().getName())) {
            return true;
        }
        setEmptyModifierIfModifierIsEmpty(nodeWithModifiers);
        return false;
    }

    private static void setEmptyModifierIfModifierIsEmpty(NodeWithModifiers nodeWithModifiers) {
        if (CollectionUtils.isEmpty(nodeWithModifiers.getModifiers())) {
            return;
        }
        Validate.isTrue(nodeWithModifiers.getModifiers().get(0) != null);
        if (Modifier.isEmpty(nodeWithModifiers.getModifiers().get(0))) {
            nodeWithModifiers.setModifiers(Lists.newArrayList(new Modifier()));
        }
    }

}
