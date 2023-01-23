package view.conversor.moeda;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import metodos.ConversorMoeda;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GraficoMoeda extends JFrame {
    private static final long serialVersionUID = 1L;

    public GraficoMoeda(String[] datas, double[] valores, String conteudoComboBoxBase, String conteudoComboBoxConversao) {
    	TimeSeriesCollection dataset = createDataset(datas, valores, conteudoComboBoxBase);
    	JFreeChart chart = createChart(dataset, conteudoComboBoxBase, conteudoComboBoxConversao);
    	ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 390));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Moeda.class.getResource("/view/conversor/moeda/grafico_icon.png")));
        add(chartPanel);
        
        configuraGrafico(conteudoComboBoxBase);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
    
    private void configuraGrafico(String conteudoComboBoxBase) {
      setSize(650, 390);
      String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxBase);	  
      
      String titulo = String.format("Valorização do(a) %s.", nomeMoedaBase);
      setTitle(titulo);
    }

    private TimeSeriesCollection createDataset(String[] datas, double[] valores, String conteudoComboBoxBase) {
        TimeSeries series = new TimeSeries(conteudoComboBoxBase);
        // Adiciona os dados de exemplo
        try {
        	for (int index = 0; index < valores.length; index++) {
        		series.add(new Day(new SimpleDateFormat("yyyy-MM-dd").parse(datas[index])), valores[index]);
        	}
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private JFreeChart createChart(TimeSeriesCollection dataset, String conteudoComboBoxBase, String conteudoComboBoxConversao) {
        String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxBase),
        		nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxConversao);
    	
    	String subTitulo = String.format("Valorização do(a) %s em relação ao %s nos últimos 30 dias.", 
        		nomeMoedaBase,
        		nomeMoedaConversao);
              
    	JFreeChart chart = ChartFactory.createTimeSeriesChart(
    		subTitulo,  // chart title
            "Data",             // x axis label
            "Valor",            // y axis label
            dataset,            // data
            true,               // include legend
            true,
            false);
    	
    	// formatando o numero para que ele apareça com 5 digitos no grafico
		XYPlot plot =  (XYPlot) chart.getPlot();
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		NumberFormat formatter = DecimalFormat.getInstance();
		formatter.setMinimumFractionDigits(5);
		range.setNumberFormatOverride(formatter);
    	
        return chart;
    }
}
