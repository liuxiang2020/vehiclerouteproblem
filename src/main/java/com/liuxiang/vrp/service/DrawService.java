package com.liuxiang.vrp.service;

import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.algorithm.draw.LineChartCreater;
import com.liuxiang.vrp.algorithm.draw.LineChartEx;
import com.liuxiang.vrp.element.City;
import com.liuxiang.vrp.element.DrawLine;
import com.liuxiang.vrp.element.Node;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.enums.LineType;

import javax.swing.*;
import java.awt.*;

public class DrawService {

    public static DrawLine createDrawLine(double[][] array){
        return DrawLine.builder()
                .figureName("算法求解迭代图")
                .title("vns求解tsp问题")
                .xAxisLabel("iteration")
                .yAxisLabel("objective")
                .iterValue(array)
                .lineType(LineType.BROKEN_LINE)
                .build();
    }

    public static DrawLine createDrawLine(Route route, TspModel model){
        double[][] coordinates = new double[route.length()+1][2];
        Node node = route.getHead();
        for (int i = 0; i <= route.length(); i++) {
            City city = model.getCityList().get(node.getIndex());
            coordinates[i][0] = city.x;
            coordinates[i][1] = city.y;
            node = node.getNext();
        }

        return DrawLine.builder()
                .figureName("算法求解路径")
                .title("优化后的路径")
                .xAxisLabel("x")
                .yAxisLabel("y")
                .iterValue(coordinates)
                .lineType(LineType.ROUTE)
                .build();
    }

    public static void drawIterObjective(double[][] array){
        DrawLine drawLine =createDrawLine(array);
        SwingUtilities.invokeLater(() -> {
            LineChartEx ex = new LineChartEx(drawLine);
            ex.setVisible(true);
        });
    }

    public static void drawRoute(Route route, TspModel model){
        DrawLine drawLine = createDrawLine(route, model);
        SwingUtilities.invokeLater(() -> {
            LineChartEx ex = new LineChartEx(drawLine);
            ex.setVisible(true);
        });
    }

    public static void drawDoubleFigure(double[][] array, Route route, TspModel model){
        DrawLine iterLine =createDrawLine(array);
        DrawLine routeLine = createDrawLine(route, model);

        JFrame jFrame = new JFrame();
        jFrame.add(LineChartCreater.createChartPanel(iterLine), BorderLayout.WEST);
        jFrame.add(LineChartCreater.createChartPanel(routeLine), BorderLayout.EAST);

        jFrame.pack();
        jFrame.setTitle("迭代图和路径图");
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
//        jFrame.setSize(1400, 600);
        jFrame.setLocationRelativeTo(null);
    }

}
