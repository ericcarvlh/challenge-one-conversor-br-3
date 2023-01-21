package view.conversor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import metodos.ConversorMoeda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;

public class GraficoMoeda extends JFrame {
    private static final long serialVersionUID = 1L;

    public GraficoMoeda(String[] datas, double[] valores, String conteudoComboBoxBase, String conteudoComboBoxConversao) {
    	TimeSeriesCollection dataset = createDataset(datas, valores, conteudoComboBoxBase);
        JFreeChart chart = createChart(dataset, conteudoComboBoxBase, conteudoComboBoxConversao);
        ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);
        
        configuraGrafico(conteudoComboBoxBase, conteudoComboBoxConversao);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
    
    private void configuraGrafico(String conteudoComboBoxBase, String conteudoComboBoxConversao) {
      setSize(600, 400);
      String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxBase),
    	nomeMoedaConversao = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxConversao);	  
      
      String titulo = String.format("Valoriza��o do(a) %s em rela��o ao(a) %s.", 
		nomeMoedaBase, 
		nomeMoedaConversao);
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
        String nomeMoedaBase = ConversorMoeda.retornaNomeMoeda(conteudoComboBoxBase);
    	
    	String descricao = String.format("Valoriza��o do(a) %s nos �ltomos 30 dias.", 
        		nomeMoedaBase);
              
    	JFreeChart chart = ChartFactory.createTimeSeriesChart(
            descricao,  // chart title
            "Data",             // x axis label
            "Valor",            // y axis label
            dataset,            // data
            true,               // include legend
            true,
            false);
        return chart;
    }

}