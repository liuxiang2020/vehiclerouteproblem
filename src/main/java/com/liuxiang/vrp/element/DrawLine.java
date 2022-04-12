package com.liuxiang.vrp.element;

import com.liuxiang.vrp.enums.LineType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class DrawLine {
    private String figureName = "算法求解迭代图";
    private String title = "求解TSP问题";
    private double[][] iterValue;
    private Map<String, double[][]> iterValueMap;
    private String xAxisLabel = "迭代次数";
    private String yAxisLabel = "目标函数值";
    private LineType lineType;

}
