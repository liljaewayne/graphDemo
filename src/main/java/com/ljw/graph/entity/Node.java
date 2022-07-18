package com.ljw.graph.entity;

import com.ljw.graph.util.DataUtil;

import java.util.Map;

public interface Node {
    String getName();

    void setName(String name);

    String getType();

    Map<String, RelationNode> 关系节点Map = DataUtil.readAllRelationNodes();
    Map<String, ConceptNode> 概念节点Map = DataUtil.readAllConceptNodes();

    static Node of(String word) {
        if (关系节点Map.containsKey(word)) {
            return 关系节点Map.get(word);
        } else if (概念节点Map.containsKey(word)) {
            return 概念节点Map.get(word);
        } else {
            return new ConceptNode(word);// NONE
        }
    }
}
