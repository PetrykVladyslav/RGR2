import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
public class Main {
    private final static DataTableBean dataTableBean = new DataTableBean();
    private static final GraphicalDataBean graphicalDataBean = new GraphicalDataBean();
    public static void main(String[] args) {
        JFrame frame = new JFrame("JavaBeans Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        JButton addButton = new JButton("Add+");
        JButton delButton = new JButton("Del-");
        JButton updateButton = new JButton("Змінити значення");
        JButton clearButton = new JButton("Очистити");
        JButton saveButton = new JButton("Зберегти");
        JButton openButton = new JButton("Відкрити");
        JButton exitButton = new JButton("Завершити");
        JTable table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(250, 400));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(tableScrollPane, BorderLayout.WEST);
        frame.getContentPane().add(graphicalDataBean, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(delButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(openButton);
        buttonPanel.add(exitButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"X", "Y"}
        );
        table.setModel(tableModel);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowData = showInputDialog(frame);
                if (rowData != null) {
                    dataTableBean.addRow(rowData);
                    updateTable(tableModel, dataTableBean.getData());
                }
            }
        });
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataTableBean.removeLastRow();
                updateTable(tableModel, dataTableBean.getData());
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataTableBean.clearData();
                updateTable(tableModel, dataTableBean.getData());
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = "data.xml";
                dataTableBean.saveXMLDoc(new File(fileName));
                JOptionPane.showMessageDialog(null, "Файл " + fileName.trim() + " збережено!", "Результати збережені", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String[] newData = showInputDialog(frame);
                    if (newData != null) {
                        dataTableBean.updateRow(selectedRow, newData);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Будь ласка, виберіть рядок для зміни", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        List<String[]> newData = DataTableBean.loadXMLDoc(file).getData();
                        dataTableBean.addData(newData);
                        updateTable(tableModel, dataTableBean.getData());
                        graphicalDataBean.setData(dataTableBean.getData());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        dataTableBean.addDataChangeListener(new DataTableBean.DataChangeListener() {
            @Override
            public void onDataChange(List<String[]> newData) {
                dataTableBean.setData(tableModel, newData);
                graphicalDataBean.setData(newData);
            }
        });
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private static void updateTable(DefaultTableModel tableModel, List<String[]> newData) {
        tableModel.setRowCount(0);
        for (String[] row : newData) {
            tableModel.addRow(row);
        }
    }
    private static String[] showInputDialog(Component parent) {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("X:"));
        inputPanel.add(xField);
        inputPanel.add(new JLabel("Y:"));
        inputPanel.add(yField);
        int result = JOptionPane.showConfirmDialog(parent, inputPanel, "Додати новий рядок", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String xValue = xField.getText();
            String yValue = yField.getText();
            return new String[]{xValue, yValue};
        }
        return null;
    }
}