package com.liuxiang.vrp.element;

import lombok.Data;

@Data
public class Arc {
    Node startNode;
    Node endNode;
    Double distance;
    Double time;

    public Arc(Node startNode, Node endNode, Double distance) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = distance;
    }
}
