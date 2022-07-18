package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.StringJoiner;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modifier implements Serializable {
    // 定语类型
//    public static final String MODIFIER_TYPE_ATTRIBUTE_MODIFIER = "attribute_modifier";

    public static final String ATTR_VALUE_MODIFIER = "attr_value_modifier";
    public static final String ATTR_ADJ_MODIFIER = "attr_adj_modifier";
    public static final String ATTR_QUANTIFIER_MODIFIER = "attr_quantifier_modifier";

    public static final String MODIFIER_TYPE_CATEGORY_MODIFIER = "category_modifier";
    public static final String MODIFIER_TYPE_NODE_MODIFIER = "node_modifier";
    public static final String MODIFIER_TYPE_FWD_ATTR_MODIFIER = "fwd_attr_modifier";
    public static final String MODIFIER_TYPE_QUANTIFIER_MODIFIER = "quantifier_modifier";
    public static final String MODIFIER_TYPE_ACT_MODIFIER = "act_modifier";
    public static final String MODIFIER_TYPE_STATUS_MODIFIER = "status_modifier";
    public static final String MODIFIER_TYPE_PREP_MODIFIER = "prep_modifier";
    public static final String MODIFIER_TYPE_REF_MODIFIER = "ref_modifier";
    public static final String MODIFIER_TYPE_ATTRIBUTE_QUERY_MODIFIER = "attribute_query_modifier";
    public static final String MODIFIER_TYPE_ADV_MODIFIER = "adv_modifier";
    public static final String MODIFIER_TYPE_SPU_SPECIAL_MODIFIER = "spu_special_modifier";


    public static final String MODIFIER_TYPE_QUERY_MODIFIER = "query_modifier";

    // 状语类型
    public static final String MODIFIER_TYPE_PREP_ADV = "prep_adv";
    public static final String MODIFIER_TYPE_PREP_ADV2 = "prep_adv2";
    public static final String MODIFIER_TYPE_ATTR_ADV = "attr_adv";
    public static final String MODIFIER_TYPE_CATE_ADV = "cate_adv";
    public static final String MODIFIER_TYPE_QUERY_ADV = "query_adv";
    public static final String MODIFIER_TYPE_ACT_ADV = "act_adv";
    public static final String MODIFIER_TYPE_DIRECTION_ADV = "direction_adv";


    // 补语类型
    public static final String MODIFIER_TYPE_PREP_COM = "prep_com";// to用,to_1用
    public static final String MODIFIER_TYPE_TEST_COM = "test_com";// to用,to_1用
    public static final String MODIFIER_TYPE_ATTR_COM = "attr_com";// 类型二：attr_com与relation node或其类节点通过attribute相连的三元组，并向下找V2节点第二层的modifier，第二层仅找negative_type
    public static final String MODIFIER_TYPE_QUERY_COM = "query_com";
    public static final String MODIFIER_TYPE_ADJ_COM = "adj_com";
    public static final String MODIFIER_TYPE_ADJ_DEGREE_COM = "adj_degree_com";

    private NodeWithModifiers nm1;
    private NodeWithModifiers nm2;
    private NodeWithModifiers nm3;

    private String modifierType;// attribute , quantifier, event, null

    public Modifier(NodeWithModifiers nm1, NodeWithModifiers nm2, NodeWithModifiers nm3) {
        this(nm1, nm2, nm3, null);
    }

    public static Modifier of(PhysicalEvent pe, String modifierType) {
        Modifier modifier = new Modifier(
                pe.getNm1(),
                pe.getNm2(),
                pe.getNm3()
        );
        modifier.setModifierType(modifierType);

        return modifier;
    }

    /**
     * 打印本层所有base的csv String
     *
     * @return
     */
    public String toTripleString() {
        StringJoiner stringJoiner = new StringJoiner(",", "{", "}");
        ensureAddBaseName(stringJoiner, nm1);
        ensureAddBaseName(stringJoiner, nm2);
        ensureAddBaseName(stringJoiner, nm3);
        return stringJoiner.toString();
    }

    /**
     * 打印所有base的csv String
     *
     * @return
     */
    public String toTripleStringRecursive() {
        LinkedList<String> str = new LinkedList<>();

        addNm(nm1, str);
        addNm(nm2, str);
        addNm(nm3, str);

        StringJoiner stringJoiner = new StringJoiner(" ", "{", "}");
        for (String s : str) {
            stringJoiner.add(s);
        }
        stringJoiner.add("type=" + modifierType);
        String peStr = stringJoiner.toString();
        peStr = StringUtils.replace(peStr, "{ }", "");
        return peStr;
    }

    private void addNm(NodeWithModifiers nm, LinkedList<String> list) {
        if (nm == null) {
            list.add("null");
            return;
        }
        if (nm.getQuery() != null) {
            list.add("<");
            Modifier query = nm.getQuery();
            list.add(query.getNm1().getBase().getName());
            list.add(query.getNm2().getBase().getName());
            list.add(query.getNm3().getBase().getName());
            list.add(">");
        }

        if (!CollectionUtils.isEmpty(nm.getModifiers())) {
            for (Modifier modifier : nm.getModifiers()) {
                list.add("{");
                addNm(modifier.getNm1(), list);
                addNm(modifier.getNm2(), list);
                addNm(modifier.getNm3(), list);
                list.add("}");
            }
        }

        if (nm.getBase() != null) {
            list.add(nm.getBase().getName());
        }
    }

    public static boolean isEmpty(Modifier modifier) {
        if (modifier == null) {
            return true;

        }

        if (NodeWithModifiers.isEmpty(modifier.getNm1())
                && NodeWithModifiers.isEmpty(modifier.getNm2())
                && NodeWithModifiers.isEmpty(modifier.getNm3())) {
            return true;
        }

        return false;
    }

    private void ensureAddBaseName(StringJoiner stringJoiner, NodeWithModifiers nm) {
        if (NodeWithModifiers.isEmpty(nm)) {
            stringJoiner.add("null");
        } else {
            stringJoiner.add(nm.getBase().getName());
        }
    }

}


