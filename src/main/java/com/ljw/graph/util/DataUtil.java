package com.ljw.graph.util;

import com.google.common.collect.Lists;
import com.ljw.graph.dao.ConceptNodeDao;
import com.ljw.graph.dao.KgTripleDao;
import com.ljw.graph.dao.RelationNodeDao;
import com.ljw.graph.entity.ConceptNode;
import com.ljw.graph.entity.KgTriple;
import com.ljw.graph.entity.Node;
import com.ljw.graph.entity.RelationNode;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataUtil {

    private static Map<String, RelationNode> allRelationNodes = new HashMap<>();// <nodeName, Node>
    private static Map<String, List<RelationNode>> relationRelationNodeDic = new HashMap<>();// <relation, List<Node>>
    private static Map<String, List<RelationNode>> peCategoryStrRelationNodeDic = new HashMap<>();// <peCategoryStr, List<Node>>
    private static Map<String, ConceptNode> allConceptNodes = new HashMap<>();

    private static List<KgTriple> kgTriples = new ArrayList<>();
    private static List<KgTriple> kgTriplesWithAlias = new ArrayList<>();
    private static Map<String, List<String>> isADic = new HashMap<>();// base, aliases


    private static RelationNodeDao relationNodeDao;
    private static ConceptNodeDao conceptNodeDao;
    private static KgTripleDao kgTripleDao;

    @Autowired
    public void setRelationNodeDao(RelationNodeDao relationNodeDao) {
        DataUtil.relationNodeDao = relationNodeDao;
    }

    @Autowired
    public void setConceptNodeDao(ConceptNodeDao conceptNodeDao) {
        DataUtil.conceptNodeDao = conceptNodeDao;
    }

    @Autowired
    public void setKgTripleDao(KgTripleDao kgTripleDao) {
        DataUtil.kgTripleDao = kgTripleDao;
    }


    public static Map<String, RelationNode> readAllRelationNodes() {

        if (MapUtils.isEmpty(allRelationNodes)) {


            List<RelationNode> 关系节点List = relationNodeDao.selectAll();
            HashMap<String, RelationNode> 关系节点Map = new HashMap<>();
            for (RelationNode relationNode : 关系节点List) {
                关系节点Map.put(
                        relationNode.getName(),
                        relationNode);
            }

            allRelationNodes = 关系节点Map;
        }
        return allRelationNodes;
    }

    public static Map<String, List<RelationNode>> readRelationRelationNodeDic() {
        if (MapUtils.isEmpty(relationRelationNodeDic)) {

            List<RelationNode> 关系节点List = relationNodeDao.selectAll();

            relationRelationNodeDic = new HashMap<>();
            for (RelationNode relationNode : 关系节点List) {
                if (relationRelationNodeDic.containsKey(relationNode.getRelation())) {
                    relationRelationNodeDic.get(relationNode.getRelation()).add(relationNode);
                } else {
                    relationRelationNodeDic.put(relationNode.getRelation(), Lists.newArrayList(relationNode));
                }

            }
        }
        return relationRelationNodeDic;
    }

    public static Map<String, List<RelationNode>> readPeCategoryStrRelationNodeDic() {
        if (MapUtils.isEmpty(peCategoryStrRelationNodeDic)) {
            Map<String, List<RelationNode>> relationRelationNodeDic = readRelationRelationNodeDic();
            for (Map.Entry<String, List<RelationNode>> entry : relationRelationNodeDic.entrySet()) {
                peCategoryStrRelationNodeDic.put(
                        RegExUtils.replaceFirst(entry.getKey(), "e_", "c_"),
                        entry.getValue()
                );
            }
        }

        return peCategoryStrRelationNodeDic;
    }

    public static Map<String, ConceptNode> readAllConceptNodes() {

        if (MapUtils.isEmpty(allConceptNodes)) {

            List<ConceptNode> 概念节点List = conceptNodeDao.selectAll();

            HashMap<String, ConceptNode> 概念节点Map = new HashMap<>();
            for (ConceptNode conceptNode : 概念节点List) {
                概念节点Map.put(
                        conceptNode.getName(),
                        conceptNode);
            }
            allConceptNodes = 概念节点Map;
        }
        return allConceptNodes;
    }


    public static List<KgTriple> readKGTriples() {
        if (CollectionUtils.isEmpty(kgTriples)) {

            kgTriples = kgTripleDao.selectAll();
        }
        return kgTriples;
    }

    public static Map<String, List<String>> getIsADic() {
        if (MapUtils.isEmpty(isADic)) {
            List<KgTriple> kgTriples = readKGTriples();

            for (KgTriple kgTriple : kgTriples) {
                String midName = kgTriple.getElement2().getName();
                if (StringUtils.equals(midName, "is_a")) {
                    String aliasName = kgTriple.getElement1().getName();
                    String originName = kgTriple.getElement3().getName();
                    if (isADic.containsKey(originName)) {
                        isADic.get(originName).add(aliasName);
                    } else {
                        isADic.put(originName, Lists.newArrayList(aliasName));
                    }
                }
            }
        }

        return isADic;
    }

    public static List<KgTriple> readKgTriplesWithAlias() {
        if (CollectionUtils.isEmpty(kgTriplesWithAlias)) {
            List<KgTriple> kgTriples = readKGTriples();
            Map<String, List<String>> isADic = getIsADic();

            kgTriplesWithAlias = new ArrayList<>();

            for (KgTriple kgTriple : kgTriples) {
                // 由 liquid_dish is_a food, 和 food attribute food_name 生成
                // 生成liquid_dish attribute food_name
                Node node1 = kgTriple.getElement1();
                Node node2 = kgTriple.getElement2();
                Node node3 = kgTriple.getElement3();
                if (!StringUtils.equals(node2.getName(), "is_a")) {
                    if (isADic.containsKey(node1.getName())) {
                        addAliasesRecurNode1(node1.getName(), kgTriplesWithAlias, node2.getName(), node3.getName());
                    }
                    if (isADic.containsKey(node3.getName())) {
                        addAliasesRecurNode3(node3.getName(), kgTriplesWithAlias, node1.getName(), node2.getName());
                    }
                }

                if (
                        (/*StringUtils.equals(node1.getType(), "RelationNode") &&*/ isADic.containsKey(node1.getName())) ||
                                (/*StringUtils.equals(node3.getType(), "RelationNode") &&*/ isADic.containsKey(node3.getName()))
                ) {

                } else {
                    kgTriplesWithAlias.add(kgTriple);
                }

            }

        }
        return kgTriplesWithAlias;
    }

    private static void addAliasesRecurNode1(String node1Name, List<KgTriple> kgTriplesWithAlias, String node2Name, String node3Name) {
        Map<String, List<String>> isADic = getIsADic();
        if (isADic.containsKey(node1Name)) {
            List<String> aliases = isADic.get(node1Name);
            for (String alias : aliases) {
                kgTriplesWithAlias.add(
                        KgTriple.of(alias + "," + node2Name + "," + node3Name)
                );
                addAliasesRecurNode1(alias, kgTriplesWithAlias, node2Name, node3Name);
            }
        }
    }


    private static void addAliasesRecurNode3(String node3Name, List<KgTriple> kgTriplesWithAlias, String node1Name, String node2Name) {
        Map<String, List<String>> isADic = getIsADic();
        if (isADic.containsKey(node3Name)) {
            List<String> aliases = isADic.get(node3Name);
            for (String alias : aliases) {
                kgTriplesWithAlias.add(
                        KgTriple.of(node1Name + "," + node2Name + "," + alias)
                );
                addAliasesRecurNode3(alias, kgTriplesWithAlias, node1Name, node2Name);
            }
        }
    }

    @SneakyThrows
    public static void write(String outputPath, List data) {
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();

        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        ) {
            bufferedWriter.write(data.toString());
            bufferedWriter.newLine();
        }

    }

    @SneakyThrows
    public static void write(String outputPath, String dataStr, boolean append) {
        File outputFile = new File(outputPath);
        if (!append) {
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, append));
        ) {
            bufferedWriter.write(dataStr);
//            bufferedWriter.newLine();
        }

    }


    public static void clear() {
        allRelationNodes = new HashMap<>();
        relationRelationNodeDic = new HashMap<>();
        peCategoryStrRelationNodeDic = new HashMap<>();
        allConceptNodes = new HashMap<>();
        kgTriples = new ArrayList<>();
        kgTriplesWithAlias = new ArrayList<>();
        isADic = new HashMap<>();
    }

}
