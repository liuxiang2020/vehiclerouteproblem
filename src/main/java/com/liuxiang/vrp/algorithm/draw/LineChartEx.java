package com.liuxiang.vrp.algorithm.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.liuxiang.vrp.element.DrawLine;
import com.liuxiang.vrp.enums.LineType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChartEx extends JFrame {

    public LineChartEx(DrawLine drawLine) {
        initUI(drawLine);
    }

    private void initUI(DrawLine drawLine) {
        XYDataset dataset;
        if(drawLine.getIterValue()==null)
            dataset = createDataset(drawLine.getIterValueMap());
        else
            dataset = createDataset(drawLine.getIterValue());
        JFreeChart chart = createChart(dataset, drawLine);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle(drawLine.getFigureName());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset(double[][] iterValue) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("algorithm", false);
        for (double[] doubles : iterValue)
            series.add(doubles[0], doubles[1]);
        series.setNotify(false);
        dataset.addSeries(series);
        return dataset;
    }

    private XYDataset createDataset(Map<String, double[][]> iterValueMap) {
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

    private JFreeChart createChart(XYDataset dataset, DrawLine drawLine) {
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

    public static void main(String[] args) {
        double[][] iterValue = {{1,10.0}, {2, 11.0}, {3, 12.0}, {4, 15.4}, {6, 19,4}, {7, 10.3}};
        Map<String, double[][]> iterValueMap = new HashMap<>();
        iterValueMap.put("vns", iterValue);
        DrawLine drawLine = DrawLine.builder()
                .figureName("算法求解迭代图")
                .title("vns求解tsp问题")
                .xAxisLabel("iteration")
                .yAxisLabel("objective")
                .iterValue(iterValue)
                .lineType(LineType.BROKEN_LINE)
                .iterValueMap(iterValueMap).build();

        SwingUtilities.invokeLater(() -> {
            LineChartEx ex = new LineChartEx(drawLine);
            ex.setVisible(true);
        });
    }
}
