package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeTrunk {
    //    private List<Node> nodes = new ArrayList<>();
    private Node element1;
    private Node element2;
    private Node element3;
    private PECategory pecategory;

    public static PeTrunk of(KgTriple kgTriple, PECategory peCategory) {
        PeTrunk peTrunk = new PeTrunk();
        peTrunk.element1 = kgTriple.getElement1();
        peTrunk.element2 = kgTriple.getElement2();
        peTrunk.element3 = kgTriple.getElement3();
        peTrunk.pecategory = peCategory;
        return peTrunk;
    }


}
