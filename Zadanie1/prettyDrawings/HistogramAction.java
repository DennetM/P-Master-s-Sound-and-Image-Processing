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
import org.jfree.chart.renderer.category.StandardBarPainter;
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
    
    public int getHistValue(String color, int i){
    	if (color == "red"){
    		return this.histogramRed[i];
    	}
    	if (color == "green"){
    		return this.histogramGreen[i];
    	}
    	if (color == "blue"){
    		return this.histogramBlue[i];
    	}
    	else {
    		System.out.println("Wrong colour code, histogram value returned at 0.\n");
    		return 0;
    	}
    }
    
    private void initiateTables(){
    	for (int i = 0; i<256; i++){
    		this.histogramRed[i] = 0;
    		this.histogramGreen[i] = 0;
    		this.histogramBlue[i] = 0;
    		this.histogramAlpha[i] = 0;
    	}
    }

    public void showTheThing(ImageReading img) {
    	BufferedImage image = img.getImage();
    	initiateTables();
    	
    	
    	for (int i=0; i<image.getWidth(); i++){
    		for (int j=0; j<image.getHeight(); j++){
    			Color tempcol = new Color(image.getRGB(i, j));
    			
    			histogramRed[tempcol.getRed()] += 1;
    			histogramGreen[tempcol.getGreen()] += 1;
    			histogramBlue[tempcol.getBlue()] += 1;
    			histogramAlpha[tempcol.getAlpha()] += 1;
    		}
    	}
        
        prepareDataset();
        createChart();
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
        barRendererRed.setDrawBarOutline(false);
        barRendererRed.setShadowVisible(false);
        barRendererRed.setGradientPaintTransformer(null);
        barRendererRed.setBarPainter(new StandardBarPainter());
        CategoryAxis categoryAxisRed = (CategoryAxis) plotRed.getDomainAxis();
        categoryAxisRed.setVisible(false);
        ValueAxis valueAxisRed = (ValueAxis) plotRed.getRangeAxis();
        valueAxisRed.setVisible(false);
        
        CategoryPlot plotGreen = chartGreen.getCategoryPlot();
        plotGreen.setBackgroundPaint(Color.white);
        BarRenderer barRendererGreen = (BarRenderer) plotGreen.getRenderer();
        barRendererGreen.setPaint(Color.green);
        barRendererGreen.setDrawBarOutline(false);
        barRendererGreen.setShadowVisible(false);
        barRendererGreen.setGradientPaintTransformer(null);
        barRendererGreen.setBarPainter(new StandardBarPainter());
        CategoryAxis categoryAxisGreen = (CategoryAxis) plotGreen.getDomainAxis();
        categoryAxisGreen.setVisible(false);
        ValueAxis valueAxisGreen = (ValueAxis) plotGreen.getRangeAxis();
        valueAxisGreen.setVisible(false);
        
        CategoryPlot plotBlue = chartBlue.getCategoryPlot();
        plotBlue.setBackgroundPaint(Color.white);
        BarRenderer barRendererBlue = (BarRenderer) plotBlue.getRenderer();
        barRendererBlue.setPaint(Color.blue);
        barRendererBlue.setDrawBarOutline(false);
        barRendererBlue.setShadowVisible(false);
        barRendererBlue.setGradientPaintTransformer(null);
        barRendererBlue.setBarPainter(new StandardBarPainter());
        CategoryAxis categoryAxisBlue = (CategoryAxis) plotBlue.getDomainAxis();
        categoryAxisBlue.setVisible(false);
        ValueAxis valueAxisBlue = (ValueAxis) plotBlue.getRangeAxis();
        valueAxisBlue.setVisible(false);
        
        CategoryPlot plotAlpha = chartAlpha.getCategoryPlot();
        plotAlpha.setBackgroundPaint(Color.white);
        BarRenderer barRendererAlpha = (BarRenderer) plotAlpha.getRenderer();
        barRendererAlpha.setPaint(Color.black);
        barRendererAlpha.setDrawBarOutline(false);
        barRendererAlpha.setShadowVisible(false);
        barRendererAlpha.setGradientPaintTransformer(null);
        barRendererAlpha.setBarPainter(new StandardBarPainter());
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
