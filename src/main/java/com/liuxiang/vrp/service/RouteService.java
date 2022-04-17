package com.liuxiang.vrp.service;

import com.liuxiang.vrp.element.Node;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.utils.MyNumber;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RouteService {
    // 距离矩阵
//    private double[][] matrix;
//
//    public RouteService(double[][] matrix) {
//        this.matrix = matrix;
//    }

    /**
     * 将indexA位置的城市插入到indexB位置后面
     * @param route
     * @param indexA
     * @param indexB
     * @return
     */
    public static double insert(Route route, int indexA, int indexB){
        if(indexA==indexB || indexA-indexB==1){
//            log.info("indexA={}, indexB={}，插入前后结果一样，无需操作", indexA, indexB);
        } else{
            int nodeAIndex = route.remove(indexA);
            if(indexA<indexB)
                route.insert(indexB-1, nodeAIndex);
            else
                route.insert(indexB, nodeAIndex);
//            log.info("将index={}插入到index={}后，路径距离为{}",indexA, indexB,  MyNumber.round(route.getDistance(),2));
        }
        if(indexA==0){
//            log.info("indexA=0, 需改变首节点序号，改变后的首节点为{}", route.getHead().getIndex());
        }

        return route.getDistance();
    }

    /**
     * 交换操作
     * @param route
     * @param indexA
     * @param indexB
     */
    public static Route swap(Route route, int indexA, int indexB){
        if(indexA==indexB) {
//            log.info("交换的两个节点的一样，无须交换");
        }  else{
            int nodeAIndex = route.getNode(indexA).getIndex();
            route.set(indexA, route.getNode(indexB).getIndex());
            route.set(indexB, nodeAIndex);
//            log.info("交换第{}个节点和第{}个节点，交换后，路径起点为{}，路径距离为{}", indexA, indexB, route.getHead().getIndex(), MyNumber.round(route.getDistance(),2));
        }
        return route;
    }

    /**
     * 逆转操作
     * @param route
     * @param indexA
     * @param indexB
     */
    public static double reverse(Route route, int indexA, int indexB){

        if(indexA > indexB){
            int temp = indexB;
            indexB = indexA;
            indexA = temp;
        }else if(indexA==indexB){
            return route.getDistance();
        }
        if((indexA==0 && indexB==route.length()-1) || (indexA==route.length()-1 && indexB==0)){
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
//        log.info("翻转第{}到{}之间的节点，翻转前，{}", indexA, indexB, route.toString());
        // 设置路径距离
        double gapDistance = calculateReversionGapDistance(route, indexA, indexB);
        route.setDistance(route.getDistance() + gapDistance);

        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        if(indexA==0)
            route.setHead(nodeB);

        Node preNodeA = nodeA.getPrev();
        Node nextNodeB = nodeB.getNext();
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
//        log.info("翻转第{}到{}之间的节点，翻转后，路径起点为{}，路径距离为{}", indexA, indexB, route.getHead().getIndex(), MyNumber.round(route.getDistance(),2));
        return route.getDistance();
    }

    public static double calculateReversionGapDistance(Route route, int indexA, int indexB){
        if(indexA==indexB)
            return Double.MAX_VALUE;
        if((indexA==0 && indexB==route.length()-1) || (indexA==route.length()-1 && indexB==0))
            return Double.MAX_VALUE;
        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        Node preNodeA = nodeA.getPrev();
        Node nextNodeB = nodeB.getNext();
        double[][] matrix = route.getMatrix();
        return matrix[preNodeA.getIndex()][nodeB.getIndex()] + matrix[nodeA.getIndex()][nextNodeB.getIndex()]
                - matrix[preNodeA.getIndex()][nodeA.getIndex()] - matrix[nodeB.getIndex()][nextNodeB.getIndex()];
    }

    public static double calculateInsertGapDistance(Route route, int indexA, int indexB){
        if(indexA==indexB || indexA-indexB == 1 || (indexA==0 && indexB==route.length()-1))
            return Double.MAX_VALUE;
        else if(indexB-indexA == 1)
            return calculateReversionGapDistance(route, indexA, indexB);

        Node nodeA = route.getNode(indexA);
        Node nodeB = route.getNode(indexB);
        double[][] matrix = route.getMatrix();
        return matrix[nodeA.getPrev().getIndex()][nodeA.getNext().getIndex()]
                + matrix[nodeB.getIndex()][nodeA.getIndex()]
                + matrix[nodeA.getIndex()][nodeB.getNext().getIndex()]
                - matrix[nodeA.getPrev().getIndex()][nodeA.getIndex()]
                - matrix[nodeA.getIndex()][nodeA.getNext().getIndex()]
                - matrix[nodeB.getIndex()][nodeB.getNext().getIndex()];
    }

    public static double calculateSwapGapDistance(Route route, int indexA, int indexB){
        double[][] matrix = route.getMatrix();
        if(indexA==indexB)
            return Double.MAX_VALUE;
        else if(Math.abs(indexA-indexB)<=2)
            return calculateReversionGapDistance(route, indexA, indexB);
        else if((indexA==0 && indexB==route.length()-1) || (indexA==route.length()-1 && indexB==0)){
            Node nodeA = route.getNode(route.length()-1);
            Node nodeB = route.getNode(0);
            Node preNodeA = nodeA.getPrev();
            Node nextNodeB = nodeB.getNext();
            // 设置路径距离
            return matrix[preNodeA.getIndex()][nodeB.getIndex()] + matrix[nodeA.getIndex()][nextNodeB.getIndex()]
                    - matrix[preNodeA.getIndex()][nodeA.getIndex()] - matrix[nodeB.getIndex()][nextNodeB.getIndex()];
        }else{
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
    }

    public static double evaluate(Route route){
        Node node = route.getHead();
        double distance = 0.0;
        double[][] matrix = route.getMatrix();
        while (node.getNext().getIndex()!=route.getHead().getIndex()){
            distance += matrix[node.getIndex()][node.getNext().getIndex()];
            node = node.getNext();
        }
        distance += matrix[node.getIndex()][node.getNext().getIndex()];
        log.info("distance={}, route.distance={}", distance, route.getDistance());
        if(Math.abs(route.getDistance()-distance)> MyNumber.MIN_VALUE)
            throw new IllegalArgumentException("领域操作函数在计算路径长度时，出错了，需要检查");
        return distance;
    }

    public static double evaluate(int[] path, double[][] matrix){
        double distance = 0.0;
        for (int i = 0; i < path.length; i++) {
            if(i==path.length-1){
                distance += matrix[path[i]][path[0]];
            }else {
                distance += matrix[path[i]][path[i+1]];
            }
        }
        return distance;
    }

    public static int[] gertPath(Route route){
        int[] path = new int[route.length()];
        for (int i = 0; i < path.length; i++) {
            path[i] = route.get(i);
        }
        return path;
    }

    public static Route copyRoute(Route route){
        int[] path = new int[route.length()];
        for (int i = 0; i < path.length; i++) {
            path[i] = route.get(i);
        }
        return new Route(path, route.getMatrix());
    }

}
