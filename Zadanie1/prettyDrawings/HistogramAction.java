package prettyDrawings;

import imageReadFunctions.ImageReading;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class HistogramAction{

    private int[] histogramRed = new int[256];
    private int[] histogramGreen = new int[256];
    private int[] histogramBlue = new int[256];
    private int[] histogramAlpha = new int[256];
    
    DefaultCategoryDataset datasetRed = new DefaultCategoryDataset();
    DefaultCategoryDataset datasetGreen = new DefaultCategoryDataset();
    DefaultCategoryDataset datasetBlue = new DefaultCategoryDataset();
    DefaultCategoryDataset datasetAlpha = new DefaultCategoryDataset();

    public BufferedImage showTheThing(ImageReading img) {
    	BufferedImage image = img.getImage();
    	

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color tempColor = new Color(image.getRGB(x, y));
                histogramRed[tempColor.getRed()]++;
                histogramGreen[tempColor.getGreen()]++;
                histogramBlue[tempColor.getBlue()]++;
                histogramAlpha[tempColor.getAlpha()]++;
                //if(y == 0)System.out.println(x + "  UJ UJ ");
                if(tempColor.getRed() == 0)System.out.println(x + "  UJ UJ ");
            }
        }
        
        prepareDataset();
        createChart();

        return null;
    }

    @SuppressWarnings("deprecation")
    private void createChart() {
        JFreeChart chartRed = ChartFactory.createBarChart(
                "Red",         // chart title
                "",               // domain axis label
                "",                  // range axis label
                datasetRed,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
            );
        JFreeChart chartGreen = ChartFactory.createBarChart(
                "Green",         // chart title
                "",               // domain axis label
                "",                  // range axis label
                datasetGreen,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
            );
        JFreeChart chartBlue = ChartFactory.createBarChart(
                "Blue",         // chart title
                "",               // domain axis label
                "",                  // range axis label
                datasetBlue,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
            );
        
        JFreeChart chartAlpha = ChartFactory.createBarChart(
                "Alpha",         // chart title
                "",               // domain axis label
                "",                  // range axis label
                datasetAlpha,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
            );
        
        chartRed.setBackgroundPaint(Color.white);
        chartGreen.setBackgroundPaint(Color.white);
        chartBlue.setBackgroundPaint(Color.white);
        chartAlpha.setBackgroundPaint(Color.white);
        
        chartRed.setBorderVisible(false);
        chartGreen.setBorderVisible(false);
        chartBlue.setBorderVisible(false);
        chartAlpha.setBorderVisible(false);
        
        CategoryPlot plotRed = chartRed.getCategoryPlot();
        plotRed.setBackgroundPaint(Color.white);
        BarRenderer barRendererRed = (BarRenderer) plotRed.getRenderer();
        barRendererRed.setPaint(Color.red);
        barRendererRed.setItemMargin(0.0);
        CategoryAxis categoryAxisRed = (CategoryAxis) plotRed.getDomainAxis();
        categoryAxisRed.setVisible(false);
        ValueAxis valueAxisRed = (ValueAxis) plotRed.getRangeAxis();
        valueAxisRed.setVisible(false);
        
        CategoryPlot plotGreen = chartGreen.getCategoryPlot();
        plotGreen.setBackgroundPaint(Color.white);
        BarRenderer barRendererGreen = (BarRenderer) plotGreen.getRenderer();
        barRendererGreen.setPaint(Color.green);
        barRendererGreen.setItemMargin(0.0);
        CategoryAxis categoryAxisGreen = (CategoryAxis) plotGreen.getDomainAxis();
        categoryAxisGreen.setVisible(false);
        ValueAxis valueAxisGreen = (ValueAxis) plotGreen.getRangeAxis();
        valueAxisGreen.setVisible(false);
        
        CategoryPlot plotBlue = chartBlue.getCategoryPlot();
        plotBlue.setBackgroundPaint(Color.white);
        BarRenderer barRendererBlue = (BarRenderer) plotBlue.getRenderer();
        barRendererBlue.setPaint(Color.blue);
        barRendererBlue.setItemMargin(0.0);
        CategoryAxis categoryAxisBlue = (CategoryAxis) plotBlue.getDomainAxis();
        categoryAxisBlue.setVisible(false);
        ValueAxis valueAxisBlue = (ValueAxis) plotBlue.getRangeAxis();
        valueAxisBlue.setVisible(false);
        
        CategoryPlot plotAlpha = chartAlpha.getCategoryPlot();
        plotAlpha.setBackgroundPaint(Color.white);
        BarRenderer barRendererAlpha = (BarRenderer) plotAlpha.getRenderer();
        barRendererAlpha.setPaint(Color.black);
        barRendererAlpha.setItemMargin(0.0);
        CategoryAxis categoryAxisAlpha = (CategoryAxis) plotAlpha.getDomainAxis();
        categoryAxisAlpha.setVisible(false);
        ValueAxis valueAxisAlpha = (ValueAxis) plotAlpha.getRangeAxis();
        valueAxisAlpha.setVisible(false);
        
        
        BufferedImage imageRed = chartRed.createBufferedImage(300, 300);
        BufferedImage imageGreen = chartGreen.createBufferedImage(300, 300);
        BufferedImage imageBlue = chartBlue.createBufferedImage(300, 300);
        BufferedImage imageAlpha = chartAlpha.createBufferedImage(300, 300);
        
        File outputfileRed = new File("imageRed.png");
        File outputfileGreen = new File("imageGreen.png");
        File outputfileBlue = new File("imageBlue.png");
        File outputfileAlpha = new File("imageAlpha.png");
        try {
            ImageIO.write(imageRed, "png", outputfileRed);
            ImageIO.write(imageGreen, "png", outputfileGreen);
            ImageIO.write(imageBlue, "png", outputfileBlue);
            ImageIO.write(imageAlpha, "png", outputfileAlpha);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    private void prepareDataset() {
        for (int i = 0; i < 256; i++) {
            datasetRed.addValue(histogramRed[i], i+"", "");
            datasetGreen.addValue(histogramGreen[i], i+"", "");
            datasetBlue.addValue(histogramBlue[i], i+"", "");
            datasetAlpha.addValue(histogramAlpha[i], i+"", "");
        }
        
    }

	

}
