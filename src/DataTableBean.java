import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement
public class DataTableBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String[]> data;
    private transient DataChangeListener listener;
    private transient PropertyChangeSupport propertySupport;
    public DataTableBean() {
        this.data = new ArrayList<>();
        this.propertySupport = new PropertyChangeSupport(this);
    }
    public void addRow(String[] rowData) {
        data.add(rowData);
        fireDataChangeEvent();
    }
    public void removeLastRow() {
        if (!data.isEmpty()) {
            data.remove(data.size() - 1);
            fireDataChangeEvent();
        }
    }
    public List<String[]> getData() {
        return data;
    }
    public void setData(DefaultTableModel tableModel, List<String[]> data) {
        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }
    public void addData(List<String[]> newData) {
        data.addAll(newData);
        fireDataChangeEvent();
    }
    public void clearData() {
        data.clear();
        fireDataChangeEvent();
    }
    public void updateRow(int index, String[] rowData) {
        if (index >= 0 && index < data.size()) {
            data.set(index, rowData);
            fireDataChangeEvent();
        }
    }
    public void saveXMLDoc(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(DataTableBean.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static DataTableBean loadXMLDoc(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(DataTableBean.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (DataTableBean) unmarshaller.unmarshal(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public void addDataChangeListener(DataChangeListener listener) {
        this.listener = listener;
    }
    public void removeDataChangeListener() {
        this.listener = null;
    }
    private void fireDataChangeEvent() {
        if (listener != null) {
            listener.onDataChange(data);
        }
        firePropertyChange("data", null, data);
    }
    public interface DataChangeListener {
        void onDataChange(List<String[]> newData);
    }
    public void setData(List<String[]> newData) {
        List<String[]> oldValue = this.data;
        this.data = newData;
        firePropertyChange("data", oldValue, newData);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertySupport().addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertySupport().removePropertyChangeListener(listener);
    }
    protected PropertyChangeSupport getPropertySupport() {
        if (propertySupport == null) {
            propertySupport = new PropertyChangeSupport(this);
        }
        return propertySupport;
    }
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        getPropertySupport().firePropertyChange(propertyName, oldValue, newValue);
    }
}