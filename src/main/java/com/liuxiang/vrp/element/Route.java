package com.liuxiang.vrp.element;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 用双向循环链表表示路径
 */
@Data
@Slf4j
public class Route {
    //路径开始节点
    Node head;
    // 记录经过的节点的个数
    int size;
    // 记录路径长度
    Double distance;
    // 距离矩阵
    double[][] matrix;

    public Route(Integer index, double[][] matrix) {
        this.head = new Node(index);
        head.prev = head;
        head.next = head;
        size=1;
        distance = matrix[head.index][head.index];
        this.matrix = matrix;
    }

    //获取长度
    public int length(){
        return size;
    }

    //获取节点
    public Node getNode(int index){
        Node node = head;
        if(index>=size/2){
            for(int i=length();i > index;i--){
                node=node.prev;
            }
            return node;
        }
        else{
            for(int i=0;i < index;i++){
                node=node.next;
            }
            return node;
        }
    }

    /**
     * 在原始路径后添加元素
     * @param element
     */
    public void add(int element){
        insert(size, element);
    }

    /**
     * 将element插入到第index个元素前
     * @param index
     * @param element
     */
    public void insert(int index, int element){
        Node node=getNode(index);
        distance = distance - matrix[node.prev.index][node.index]
                + matrix[node.prev.index][element] + matrix[element][node.index];

        Node newNode=new Node(element, node.prev, node);

        node.prev.next=newNode;
        node.prev=newNode;
        size++;
    }

    public int remove(int index){
        Node node = getNode(index);
        int data = node.index;
        distance = distance - matrix[node.prev.index][node.index] - matrix[node.index][node.next.index]
                + matrix[node.prev.index][node.next.index];
        node.prev.next=node.next;
        node.next.prev=node.prev;
        size--;
        return data;
    }

    //获取i位置的数据
    public int get(int i){
        return getNode(i).index;
    }

    //为i位置元素重新赋值
    public int set(int index, int value){
        Node node = getNode(index);
        distance = distance - matrix[node.prev.index][node.index] - matrix[node.index][node.next.index]
                + matrix[node.prev.index][value] + matrix[value][node.next.index];
        int old = node.index;
        node.index = value;
        return old;
    }

    public void print(){
        for(int i=0;i<size;i++){
            System.out.println(getNode(i).toString());
        }
    }

    @Override
    public String toString(){
        StringBuilder route = new StringBuilder(String.format("路径的距离为: %.2f, 拜访顺序为: [", distance));
        Node node = head;
        for (int i = 0; i < size; i++) {
            route.append(node.getIndex()).append(", ");
            node = node.getNext();
        }
        route.append("]");
        return route.toString();
    }

}
