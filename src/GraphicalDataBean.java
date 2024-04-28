import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.List;
public class GraphicalDataBean extends JPanel implements Serializable {
    private List<String[]> data;
    private PropertyChangeSupport propertySupport;
    private static final long serialVersionUID = 1L;
    public GraphicalDataBean() {
        setLayout(new BorderLayout());
        propertySupport = new PropertyChangeSupport(this);
        updateGraph();
    }
    public void setData(List<String[]> data) {
        List<String[]> oldValue = this.data;
        this.data = data;
        propertySupport.firePropertyChange("data", oldValue, data);
        updateGraph();
    }
    public List<String[]> getData() {
        return data;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    private void updateGraph() {
        removeAll();
        XYSeries series = new XYSeries("Data");
        if (data != null) {
            for (String[] point : data) {
                try {
                    double x = Double.parseDouble(point[0]);
                    double y = Double.parseDouble(point[1]);
                    series.add(x, y);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Помилка при конвертації значень у числа: " + e.getMessage() + ". Видаліть рядок.");
                }
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Graph", "X", "Y",
                dataset, PlotOrientation.VERTICAL,
                true, true, false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 300));
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}