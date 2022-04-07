package com.liuxiang.vrp.element;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    Node prev;
    int index;
    Node next;

    public Node (int nodeCode) { this.index = nodeCode;
    }

    public Node (int data, Node prev,  Node next) {
        this.prev = prev;
        this.index = data;
        this.next = next;
    }

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>();
        values.add(4);
        values.add(8);
        int v = values.get(1);
        System.out.println(v);
    }

    @Override
    public String toString(){
        return String.format("pre=%d, index=%d, next=%d", prev.index, index, next.index);
    }

}
