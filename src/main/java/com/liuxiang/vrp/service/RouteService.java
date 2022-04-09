package com.liuxiang.vrp.service;

import com.liuxiang.vrp.element.Node;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.utils.Epsilon;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteService {
    // 距离矩阵
    double[][] matrix;

    public RouteService(double[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * 将indexA位置的城市插入到indexB位置后面
     * @param route
     * @param indexA
     * @param indexB
     * @return
     */
    public double insert(Route route, int indexA, int indexB){
        if(indexA==indexB || indexA-indexB==1){
            log.info("indexA={}, indexB={}，插入前后结果一样，无需操作", indexA, indexB);
        } else{
            int nodeAIndex = route.remove(indexA);
            if(indexA<indexB)
                route.insert(indexB-1, nodeAIndex);
            else
                route.insert(indexB, nodeAIndex);
            log.info("将index={}插入到index={}后，{}",indexA, indexB, route.toString());
        }
        if(indexA==0)
            log.info("indexA=0, 需改变首节点序号，改变后的首节点为{}", route.getHead().getIndex());
        return route.getDistance();
    }

    /**
     * 交换操作
     * @param route
     * @param indexA
     * @param indexB
     */
    public double swap(Route route, int indexA, int indexB){
        if(indexA==indexB) {
            log.info("交换的两个节点的一样，无须交换");
        } else if(Math.abs(indexA-indexB)<=2){
            log.info("交换的两个节点序号的差值小于等于2，采用翻转操作");
            reversion(route, indexA, indexB);
        } else{
            int nodeAIndex = route.getNode(indexA).getIndex();
            route.set(indexA, route.getNode(indexB).getIndex());
            route.set(indexB, nodeAIndex);
            log.info("交换第{}个节点和第{}个节点，交换后，路径起点为{}，{}", indexA, indexB, route.getHead().getIndex(), route.toString());
        }
        return route.getDistance();
    }

    /**
     * 逆转操作
     * @param route
     * @param indexA
     * @param indexB
     */
    public double reversion(Route route, int indexA, int indexB){
        log.info("翻转第{}到{}之间的节点，翻转前，{}", indexA, indexB, route.toString());
        if(indexA > indexB){
            int temp = indexB;
            indexB = indexA;
            indexA = temp;
        }else if(indexA==indexB){
            log.info("indexA与indexB相同，不用任何处理");
            return route.getDistance();
        }
        if(indexA==0 && indexB==route.length()-1){
            log.info("翻转首节点和尾节点之间的路径，相当于只是方向行走，相当于未翻转，不做任务处理直接退出");
//        if(indexA==0 && indexB==route.length()-1){
//            for (int i = 0; i <= indexB; i++) {
//                tempNode = nodeA.getNext();
//                nodeA.setNext(nodeA.getPrev());
//                nodeA.setPrev(tempNode);
//                nodeA = tempNode;
//            }
//        }
            return route.getDistance();
        }

        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);

        if(indexA==0)
            route.setHead(nodeB);

        Node preNodeA = nodeA.getPrev();
        Node nextNodeB = nodeB.getNext();
        // 设置路径距离
        double gapDistance = matrix[preNodeA.getIndex()][nodeB.getIndex()] + matrix[nodeA.getIndex()][nextNodeB.getIndex()]
                - matrix[preNodeA.getIndex()][nodeA.getIndex()] - matrix[nodeB.getIndex()][nextNodeB.getIndex()];
        route.setDistance(route.getDistance() + gapDistance);

        Node lastTemp = nodeA;
        Node tempNode = nodeA.getNext();

        for(int i=indexA; i<=indexB; i++){
            if(i==indexA){
                nodeA.setPrev(tempNode);
                nodeA.setNext(nextNodeB);
            } else if(i==indexB){
                nodeB.setNext(lastTemp);
                nodeB.setPrev(preNodeA);
            } else{
                Node nextTemp = tempNode.getNext();
                tempNode.setNext(lastTemp);
                tempNode.setPrev(nextTemp);
                lastTemp = tempNode;
                tempNode = nextTemp;
            }
        }
        nextNodeB.setPrev(nodeA);
        preNodeA.setNext(nodeB);

        log.info("翻转第{}到{}之间的节点，翻转后，路径起点为{}，{}", indexA, indexB, route.getHead().getIndex(), route.toString());

        return route.getDistance();
    }

    public double calculateReversionGapDistance(Route route, int indexA, int indexB){
        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        Node preNodeA = nodeA.getPrev();
        Node nextNodeB = nodeB.getNext();
        // 设置路径距离
        return matrix[preNodeA.getIndex()][nodeB.getIndex()] + matrix[nodeA.getIndex()][nextNodeB.getIndex()]
                - matrix[preNodeA.getIndex()][nodeA.getIndex()] - matrix[nodeB.getIndex()][nextNodeB.getIndex()];
    }

    public double calculateInsertGapDistance(Route route, int indexA, int indexB){
        if(indexA==indexB)
            return 0.0;
        if(Math.abs(indexA-indexB) == 1)
            return calculateReversionGapDistance(route, indexA, indexB);

        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        return matrix[nodeA.getPrev().getIndex()][nodeA.getNext().getIndex()]
                + matrix[nodeB.getIndex()][nodeA.getIndex()]
                + matrix[nodeA.getIndex()][nodeB.getNext().getIndex()]
                - matrix[nodeA.getPrev().getIndex()][nodeA.getIndex()]
                - matrix[nodeA.getIndex()][nodeA.getNext().getIndex()]
                - matrix[nodeB.getIndex()][nodeB.getNext().getIndex()];
    }

    public double calculateSwapGapDistance(Route route, int indexA, int indexB){

        if(indexA==indexB)
            return 0.0;
        if(Math.abs(indexA-indexB)<=2)
            return calculateReversionGapDistance(route, indexA, indexB);

        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        return matrix[nodeA.getPrev().getIndex()][nodeB.getIndex()]
                + matrix[nodeB.getIndex()][nodeA.getNext().getIndex()]
                + matrix[nodeB.getPrev().getIndex()][nodeA.getIndex()]
                + matrix[nodeA.getIndex()][nodeB.getNext().getIndex()]
                - matrix[nodeA.getPrev().getIndex()][nodeA.getIndex()]
                - matrix[nodeA.getIndex()][nodeA.getNext().getIndex()]
                - matrix[nodeB.getPrev().getIndex()][nodeB.getIndex()]
                - matrix[nodeB.getIndex()][nodeB.getNext().getIndex()];
    }

    public double evaluate(Route route){
        Node node = route.getHead();
        double distance = 0.0;

        while (node.getNext().getIndex()!=route.getHead().getIndex()){
            distance += matrix[node.getIndex()][node.getNext().getIndex()];
            node = node.getNext();
        }
        distance += matrix[node.getIndex()][node.getNext().getIndex()];
        log.info("distance={}, route.distance={}", distance, route.getDistance());
        if(Math.abs(route.getDistance()-distance)> Epsilon.MIN_VALUE)
            throw new IllegalArgumentException("领域操作函数在计算路径长度时，出错了，需要检查");
        return distance;
    }
}
