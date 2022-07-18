package com.ljw.graph.entity;

public enum PECategory {
    MAKER_ACT_TAKER("maker", "c_act", "taker"),
    MAKER_PREP_TAKER("maker", "c_prep", "taker"),
    MAKER_ACT_EVENT("maker", "c_act", "event"),
    EVENT_ACT_TAKER("event", "c_act", "taker"),

    MAKER_ACTPREP_TAKER("maker", "c_actprep", "taker"),
    MAKER_STATUS_TAKER("maker", "c_status", "taker"),

    MAKER_ADJECTIVE_SOME_ADJ("maker", "c_adjective", "some_adj"),
    EVENT_ADJECTIVE_SOME_ADJ("event", "c_adjective", "some_adj"),

    MAKER_INDIRECT_SOME_ADJ("maker", "c_indirect", "some_adj"),
    MAKER_VALUE_SOME_VALUE("maker", "c_value", "some_value"),
    THERE_IS_TAKER(null, "c_there_is", "taker"),
    MAKER_IS_TAKER("maker", "c_is", "taker"),
    MAKER_IS_EVENT("maker", "c_is", "event"),

    NODE_TRUNK("unmentioned", "unmentioned", "x"),// 未明确: 未指定三元组中每个单词的category
    /*
    nm1的类型为c_adj_entity
        nm1不找modifier
        nm1找modifier
        X-adjective-some_adj
        nm2、nm3分别为for_1 some_extent

        实例化：
        some_adj some_extent

        X some_adj some_extent

        例：[food_flavor adjective]some_food_flavor for_1 some_extent
     */
    SOME_ADJ_PREP_SOME_EXTENT("c_adj_entity", "for_1", "some_extent"),

    NONE(null, null, null);

    public final String v1;
    public final String v2;
    public final String v3;

    PECategory(String v1, String v2, String v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }


    public String get(int i) {

        if (i == 0) {
            return v1;
        } else if (i == 1) {
            return v2;
        } else if (i == 2) {
            return v3;
        } else {
            return null;
        }

    }
}
