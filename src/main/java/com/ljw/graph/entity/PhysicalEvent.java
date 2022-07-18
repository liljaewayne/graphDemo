package com.ljw.graph.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PhysicalEvent implements Serializable {
    private NodeWithModifiers nm1;
    private NodeWithModifiers nm2;
    private NodeWithModifiers nm3;
    private PECategory pecategory;
    private Boolean obj_direct = true;

    public PhysicalEvent(NodeWithModifiers nm1, NodeWithModifiers nm2, NodeWithModifiers nm3, PECategory pecategory) {
        this.nm1 = nm1;
        this.nm2 = nm2;
        this.nm3 = nm3;
        this.pecategory = pecategory;
    }

    public static boolean isEmpty(PhysicalEvent physicalEvent) {
        if (physicalEvent == null) {
            return true;
        }
        return physicalEvent.getNm1().getBase() == null && physicalEvent.getNm2().getBase() == null && physicalEvent.getNm3().getBase() == null;
    }
}
