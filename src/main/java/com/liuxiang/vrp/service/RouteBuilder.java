package com.liuxiang.vrp.service;

import com.liuxiang.vrp.element.Node;

import java.util.ArrayList;
import java.util.List;

public class RouteBuilder {
    public  static Node setHead(int[] path){
        List<Node> nodeList = new ArrayList<>();
        for (int j : path) {
            nodeList.add(new Node(j));
        }
        for (int i = 0; i < path.length; i++) {
            int preIndex = i-1;
            int nextIndex = i+1;
            if(i==0){
                preIndex = path.length-1;
            }else if(i==path.length-1){
                nextIndex = 0;
            }
            nodeList.get(i).setPrev(nodeList.get(preIndex));
            nodeList.get(i).setNext(nodeList.get(nextIndex));
        }
        return nodeList.get(0);
    }
}
