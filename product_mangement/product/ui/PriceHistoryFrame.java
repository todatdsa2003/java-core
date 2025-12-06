package ui;

import dao.ProductDAOImpl;
import dao.ProductPriceHistoryDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Product;
import models.ProductPriceHistory;

public class PriceHistoryFrame extends JFrame {
    
    private ProductPriceHistoryDAO priceHistoryDAO;
    private ProductDAOImpl productDAO;
    
    private JComboBox<Product> cboFilterProduct;
    private JTable tblPriceHistory;
    private DefaultTableModel tableModel;
    private JButton btnFilter;
    
    public PriceHistoryFrame() {
        priceHistoryDAO = new ProductPriceHistoryDAO();
        productDAO = new ProductDAOImpl();
        
        setTitle("Lich su Thay doi Gia");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadTableData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createTablePanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        panel.add(new JLabel("Loc theo san pham:"));
        cboFilterProduct = new JComboBox<>();
        cboFilterProduct.setPreferredSize(new Dimension(300, 25));
        panel.add(cboFilterProduct);
        
        btnFilter = new JButton("Loc");
        btnFilter.addActionListener(e -> filterByProduct());
        panel.add(btnFilter);
        
        JButton btnShowAll = new JButton("Hien thi tat ca");
        btnShowAll.addActionListener(e -> loadTableData());
        panel.add(btnShowAll);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Lich su thay doi gia"));
        
        String[] columnNames = {"ID", "San pham", "Gia cu", "Gia moi", "Thoi gian thay doi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPriceHistory = new JTable(tableModel);
        tblPriceHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tblPriceHistory);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadTableData() {
        loadComboBoxData();
        
        tableModel.setRowCount(0);
        List<ProductPriceHistory> historyList = priceHistoryDAO.findAll();
        
        for (ProductPriceHistory history : historyList) {
            Object[] row = {
                history.getId(),
                history.getProductName() != null ? history.getProductName() : "",
                history.getOldPrice(),
                history.getNewPrice(),
                history.getChangedAt()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadComboBoxData() {
        cboFilterProduct.removeAllItems();
        cboFilterProduct.addItem(null);
        
        List<Product> products = productDAO.findAll();
        for (Product product : products) {
            cboFilterProduct.addItem(product);
        }
    }
    
    private void filterByProduct() {
        Product selectedProduct = (Product) cboFilterProduct.getSelectedItem();
        if (selectedProduct == null) {
            loadTableData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<ProductPriceHistory> historyList = priceHistoryDAO.findByProductId(selectedProduct.getId());
        
        for (ProductPriceHistory history : historyList) {
            Object[] row = {
                history.getId(),
                history.getProductName() != null ? history.getProductName() : "",
                history.getOldPrice(),
                history.getNewPrice(),
                history.getChangedAt()
            };
            tableModel.addRow(row);
        }
    }
}
