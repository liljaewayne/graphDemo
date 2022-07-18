package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * triple
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KgTriple implements Serializable {
    private Integer id;

    private String node1;

    private String node2;

    private String node3;

    private Node element1;
    private Node element2;
    private Node element3;
    private PECategory pecategory;

    /**
     * @param names 逗号分割的nodeNames
     * @return
     */
    public static KgTriple of(String names) {
        KgTriple kgTriple = new KgTriple();

        String[] split = names.split(",");
        kgTriple.setElement1(Node.of(split[0]));
        kgTriple.setElement2(Node.of(split[1]));
        if (split.length < 3) {
            kgTriple.setElement3(Node.of(""));
        } else {
            kgTriple.setElement3(Node.of(split[2]));
        }

        return kgTriple;

    }

    public void setNode1(String node1) {
        this.node1 = node1;
        setElement1(Node.of(node1));
    }

    public void setNode2(String node2) {
        this.node2 = node2;
        setElement2(Node.of(node2));
    }

    public void setNode3(String node3) {
        this.node3 = node3;
        setElement3(Node.of(node3));
    }


    public String toTripleString() {
        return element1.getName() + "," + element2.getName() + "," + element3.getName();
    }
}