package com.ljw.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * pecategory_create
 * @author 
 */
@Data
public class PecategoryCreate implements Serializable {

    private String node1;

    private String node2;

    private String node3;

    private String pecategory;

    private Boolean objDirect = false;

    public PecategoryCreate(String node1, String node2, String node3, String pecategory) {
        this.node1 = node1;
        this.node2 = node2;
        this.node3 = node3;
        this.pecategory = pecategory;
    }
}