package com.liuxiang.vrp.algorithm.draw;

import java.awt.*;
import java.util.Map;

import com.liuxiang.vrp.element.DrawLine;
import org.jfree.chart.*;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class LineChartCreater {
    public static XYDataset createDataset(double[][] iterValue) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("algorithm", false);
        for (double[] doubles : iterValue)
            series.add(doubles[0], doubles[1]);
        series.setNotify(false);
        dataset.addSeries(series);
        return dataset;
    }

    public static XYDataset createDataset(Map<String, double[][]> iterValueMap) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(Map.Entry<String, double[][]> entry: iterValueMap.entrySet()){
            XYSeries series = new XYSeries(entry.getKey(), false);
            double[][] iterValue = entry.getValue();
            for (double[] doubles : iterValue)
                series.add(doubles[0], doubles[1]);
            series.setNotify(false);
            dataset.addSeries(series);
        }
        return dataset;
    }

    public static JFreeChart createChart(XYDataset dataset, DrawLine drawLine) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                drawLine.getTitle(),
                drawLine.getXAxisLabel(),
                drawLine.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.setTitle(new TextTitle(drawLine.getTitle(),
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );
        return chart;
    }

    public static ChartPanel createChartPanel(DrawLine drawLine){
        XYDataset dataSet = LineChartCreater.createDataset(drawLine.getIterValue());
        JFreeChart chart = LineChartCreater.createChart(dataSet, drawLine);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        chartPanel.setBackground(Color.white);
        return chartPanel;
    }
}