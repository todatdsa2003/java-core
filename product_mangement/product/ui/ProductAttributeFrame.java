package ui;

import dao.ProductAttributeDAO;
import dao.ProductDAOImpl;
import models.ProductAttribute;
import models.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductAttributeFrame extends JFrame {
    
    private ProductAttributeDAO attributeDAO;
    private ProductDAOImpl productDAO;
    
    private JTextField txtId, txtAttributeName, txtAttributeValue;
    private JComboBox<Product> cboProduct;
    private JComboBox<Product> cboFilterProduct;
    
    private JTable tblAttributes;
    private DefaultTableModel tableModel;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnFilter;
    
    public ProductAttributeFrame() {
        attributeDAO = new ProductAttributeDAO();
        productDAO = new ProductDAOImpl();
        
        setTitle("Quan ly Thuoc tinh San pham");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadTableData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = createTopPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        panel.add(new JLabel("Loc theo san pham:"));
        cboFilterProduct = new JComboBox<>();
        cboFilterProduct.setPreferredSize(new Dimension(250, 25));
        panel.add(cboFilterProduct);
        
        btnFilter = new JButton("Loc");
        btnFilter.addActionListener(e -> filterByProduct());
        panel.add(btnFilter);
        
        JButton btnShowAll = new JButton("Hien thi tat ca");
        btnShowAll.addActionListener(e -> loadTableData());
        panel.add(btnShowAll);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thong tin thuoc tinh"));
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        fieldsPanel.add(txtId, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("*San pham:"), gbc);
        gbc.gridx = 1;
        cboProduct = new JComboBox<>();
        fieldsPanel.add(cboProduct, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("*Ten thuoc tinh:"), gbc);
        gbc.gridx = 1;
        txtAttributeName = new JTextField(20);
        fieldsPanel.add(txtAttributeName, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("*Gia tri:"), gbc);
        gbc.gridx = 1;
        txtAttributeValue = new JTextField(20);
        fieldsPanel.add(txtAttributeValue, gbc);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdd = new JButton("Them");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.BLACK);
        btnAdd.addActionListener(e -> addAttribute());
        
        btnUpdate = new JButton("Cap nhat");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.addActionListener(e -> updateAttribute());
        
        btnDelete = new JButton("Xoa");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.BLACK);
        btnDelete.addActionListener(e -> deleteAttribute());
        
        btnClear = new JButton("Lam moi");
        btnClear.setBackground(new Color(149, 165, 166));
        btnClear.setForeground(Color.BLACK);
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Danh sach thuoc tinh"));
        
        String[] columnNames = {"ID", "San pham", "Ten thuoc tinh", "Gia tri"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblAttributes = new JTable(tableModel);
        tblAttributes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAttributes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblAttributes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadTableData() {
        loadComboBoxData();
        
        tableModel.setRowCount(0);
        List<ProductAttribute> attributes = attributeDAO.findAll();
        
        for (ProductAttribute attr : attributes) {
            Object[] row = {
                attr.getId(),
                attr.getProductName() != null ? attr.getProductName() : "",
                attr.getAttributeName(),
                attr.getAttributeValue()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadComboBoxData() {
        cboProduct.removeAllItems();
        cboFilterProduct.removeAllItems();
        cboFilterProduct.addItem(null);
        
        List<Product> products = productDAO.findAll();
        for (Product p : products) {
            cboProduct.addItem(p);
            cboFilterProduct.addItem(p);
        }
    }
    
    private void filterByProduct() {
        Product selectedProduct = (Product) cboFilterProduct.getSelectedItem();
        if (selectedProduct == null) {
            loadTableData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<ProductAttribute> attributes = attributeDAO.findByProductId(selectedProduct.getId());
        
        for (ProductAttribute attr : attributes) {
            Object[] row = {
                attr.getId(),
                attr.getProductName() != null ? attr.getProductName() : "",
                attr.getAttributeName(),
                attr.getAttributeValue()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadSelectedRowToForm() {
        int selectedRow = tblAttributes.getSelectedRow();
        if (selectedRow == -1) return;
        
        long attributeId = (Long) tableModel.getValueAt(selectedRow, 0);
        ProductAttribute attribute = attributeDAO.findById(attributeId);
        
        if (attribute != null) {
            txtId.setText(String.valueOf(attribute.getId()));
            txtAttributeName.setText(attribute.getAttributeName());
            txtAttributeValue.setText(attribute.getAttributeValue());
            
            setProductSelection(attribute.getProductId());
        }
    }
    
    private void setProductSelection(Long productId) {
        if (productId == null) return;
        
        for (int i = 0; i < cboProduct.getItemCount(); i++) {
            Product item = cboProduct.getItemAt(i);
            if (item != null && item.getId().equals(productId)) {
                cboProduct.setSelectedIndex(i);
                return;
            }
        }
    }
    
    private void addAttribute() {
        try {
            if (!validateForm()) return;
            
            ProductAttribute attribute = new ProductAttribute();
            
            Product selectedProduct = (Product) cboProduct.getSelectedItem();
            attribute.setProductId(selectedProduct.getId());
            attribute.setAttributeName(txtAttributeName.getText().trim());
            attribute.setAttributeValue(txtAttributeValue.getText().trim());
            
            boolean success = attributeDAO.insert(attribute);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Them thuoc tinh thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Them thuoc tinh that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAttribute() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long chon thuoc tinh can cap nhat!", "Canh bao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validateForm()) return;
            
            ProductAttribute attribute = new ProductAttribute();
            attribute.setId(Long.parseLong(txtId.getText().trim()));
            
            Product selectedProduct = (Product) cboProduct.getSelectedItem();
            attribute.setProductId(selectedProduct.getId());
            attribute.setAttributeName(txtAttributeName.getText().trim());
            attribute.setAttributeValue(txtAttributeValue.getText().trim());
            
            boolean success = attributeDAO.update(attribute);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cap nhat thuoc tinh thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Cap nhat thuoc tinh that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAttribute() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long chon thuoc tinh can xoa!", "Canh bao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Ban co chac chan muon xoa thuoc tinh nay?", "Xac nhan xoa", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) return;
            
            long attributeId = Long.parseLong(txtId.getText().trim());
            boolean success = attributeDAO.delete(attributeId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Xoa thuoc tinh thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xoa thuoc tinh that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtAttributeName.setText("");
        txtAttributeValue.setText("");
        if (cboProduct.getItemCount() > 0) {
            cboProduct.setSelectedIndex(0);
        }
        tblAttributes.clearSelection();
    }
    
    private boolean validateForm() {
        if (cboProduct.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon san pham!", "Loi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (txtAttributeName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ten thuoc tinh khong duoc de trong!", "Loi", JOptionPane.WARNING_MESSAGE);
            txtAttributeName.requestFocus();
            return false;
        }
        
        if (txtAttributeValue.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Gia tri khong duoc de trong!", "Loi", JOptionPane.WARNING_MESSAGE);
            txtAttributeValue.requestFocus();
            return false;
        }
        
        return true;
    }
}
