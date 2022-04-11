package com.liuxiang.vrp.element;

import lombok.Data;

import java.io.Serializable;

@Data
public class Node implements Serializable {
    Node prev;
    int index;
    Node next;

    public Node (int nodeCode) { this.index = nodeCode; }

    public Node (int data, Node prev,  Node next) {
        this.prev = prev;
        this.index = data;
        this.next = next;
    }

    @Override
    public String toString(){
        return String.format("pre=%d, index=%d, next=%d", prev.index, index, next.index);
    }

}
