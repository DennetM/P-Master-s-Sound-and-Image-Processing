package prettyDrawings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.ApplicationFrame;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

public class histogramy{
	
	public HistogramDataset datasetHist;
	public JFreeChart histogram;
	public double [][] image;
	
	
	public void extracted3(final ChartPanel chartPanel) {
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	}
	
	public HistogramDataset createDatasetHist(ArrayList a) {
        

        int licznik = 0;
        final double[] tablicaWartosci;
        XYSeries series = new XYSeries("XYGraph");
        Iterator iterator = a.iterator();
        while (iterator.hasNext()){
        	series.add((XYDataItem) iterator.next());
        	licznik++;
        }
        tablicaWartosci = new double[series.getItemCount()];
        for (int i = 0; i < series.getItemCount(); i++){
        	tablicaWartosci[i] = Double.parseDouble(String.valueOf(series.getY(i)));
        }
        
        datasetHist = new HistogramDataset();
        datasetHist.setType(HistogramType.RELATIVE_FREQUENCY);
        datasetHist.addSeries("Histogram", tablicaWartosci, 10);
        
        return datasetHist;
        
    }




	public JFreeChart createHist(final HistogramDataset dataset, String nazwa) {
		
		
		histogram = ChartFactory.createHistogram("Histogram", 
				"Przedzialy", 
				"Ilosc", 
				datasetHist, 
				PlotOrientation.VERTICAL, 
				false, 
				false, 
				false
				);
		histogram.setAntiAlias(true);
		
		histogram.setBackgroundPaint(Color.white);
		histogram.getTitle().setPaint(Color.black);
	
		return histogram;
	
	}
	
	public ArrayList ConvToArray(double[][] tab){
		ArrayList result = new ArrayList();
		XYSeries temp = new XYSeries("Seria");
		
		for (int i=0; i<tab.length;i++){
			temp.add(tab[i][0],tab[i][1]);
		}
		result.clear();
		result.addAll(temp.getItems());
		
		return result;
	}
	
	public ArrayList ConvToArray2(double[] a, double[] b){
		ArrayList result = new ArrayList();
		XYSeries temp = new XYSeries("Seria");
		
		for (int i=0; i<a.length && i<b.length;i++){	
			temp.add(a[i],b[i]);		
		}
		result.clear();
		result.addAll(temp.getItems());
		
		return result;
	}
	
}
