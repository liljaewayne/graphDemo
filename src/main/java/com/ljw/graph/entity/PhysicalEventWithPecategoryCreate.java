package com.ljw.graph.entity;

import com.ljw.graph.util.IdHolder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.StringJoiner;

@Data
public class PhysicalEventWithPecategoryCreate implements Serializable {
    private NodeWithModifiers nm1;
    private NodeWithModifiers nm2;
    private NodeWithModifiers nm3;
    private PECategory pecategory;
    private Boolean obj_direct;

    private String 主干node1;
    private String 主干node2;
    private String 主干node3;

    public PhysicalEventWithPecategoryCreate(PecategoryCreate pecategoryCreate) {
        主干node1 = pecategoryCreate.getNode1();
        主干node2 = pecategoryCreate.getNode2();
        主干node3 = pecategoryCreate.getNode3();
        pecategory = PECategory.valueOf(pecategoryCreate.getPecategory());
        obj_direct = pecategoryCreate.getObjDirect();
    }

    public static boolean isEmpty(PhysicalEventWithPecategoryCreate physicalEventWithPecategoryCreate) {
        if (physicalEventWithPecategoryCreate == null) {
            return true;
        }

        return physicalEventWithPecategoryCreate.getNm1().getBase() == null
                && physicalEventWithPecategoryCreate.getNm2().getBase() == null
                && physicalEventWithPecategoryCreate.getNm3().getBase() == null;
    }

    /**
     * id,PE,type,node1,node2,node3
     */
    public String toCsvString() {
        StringJoiner stringJoiner = new StringJoiner(",");

        stringJoiner.add("" + IdHolder.get("PhysicalEventWithPecategoryCreate"));
        stringJoiner.add(toPeStr());
        stringJoiner.add(pecategory.toString());
        stringJoiner.add(主干node1);
        stringJoiner.add(主干node2);
        stringJoiner.add(主干node3);

        return stringJoiner.toString();

    }

    private String toPeStr() {

        LinkedList<String> str = new LinkedList<>();

        addNmToStr(nm1, str);
        addNmToStr(nm2, str);
        addNmToStr(nm3, str);

        StringJoiner stringJoiner = new StringJoiner(" ");
        for (String s : str) {
            stringJoiner.add(s);
        }
        String peStr = stringJoiner.toString();
        peStr = StringUtils.replace(peStr, "[ ]", "");

        return peStr;

    }

    private void addNmToStr(NodeWithModifiers nm, LinkedList<String> list) {
        if (nm == null) {
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
                list.add("[");
                addNmToStr(modifier.getNm1(), list);
                addNmToStr(modifier.getNm2(), list);
                addNmToStr(modifier.getNm3(), list);
                list.add("]");
            }

        }


        if (nm.getBase() != null) {
            list.add(nm.getBase().getName());
        }
    }

}
