package ui;

import dao.CategoryDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Category;

public class CategoryManagementFrame extends JFrame {
    
    private CategoryDAO categoryDAO;
    
    private JTextField txtId, txtName, txtSlug;
    private JComboBox<Category> cboParentCategory;
    
    private JTable tblCategories;
    private DefaultTableModel tableModel;
    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    
    public CategoryManagementFrame() {
        categoryDAO = new CategoryDAO();
        
        setTitle("Quan ly Danh muc");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadTableData();
        
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thong tin danh muc"));
        
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
        fieldsPanel.add(new JLabel("*Ten danh muc:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        fieldsPanel.add(txtName, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("*Slug:"), gbc);
        gbc.gridx = 1;
        txtSlug = new JTextField(20);
        fieldsPanel.add(txtSlug, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("Mo ta:"), gbc);
        gbc.gridx = 1;
        
        gbc.gridx = 0; gbc.gridy = row;
        fieldsPanel.add(new JLabel("Danh muc cha:"), gbc);
        gbc.gridx = 1;
        cboParentCategory = new JComboBox<>();
        fieldsPanel.add(cboParentCategory, gbc);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdd = new JButton("Them");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.BLACK);
        btnAdd.addActionListener(e -> addCategory());
        
        btnUpdate = new JButton("Cap nhat");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.addActionListener(e -> updateCategory());
        
        btnDelete = new JButton("Xoa");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.BLACK);
        btnDelete.addActionListener(e -> deleteCategory());
        
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
        panel.setBorder(BorderFactory.createTitledBorder("Danh sach danh muc"));
        
        String[] columnNames = {"ID", "Ten danh muc", "Slug", "Danh muc cha"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCategories = new JTable(tableModel);
        tblCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCategories.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblCategories);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadTableData() {
        loadComboBoxData();
        
        tableModel.setRowCount(0);
        List<Category> categories = categoryDAO.findAll();
        
        for (Category c : categories) {
            String parentName = "";
            if (c.getParentId() != null) {
                Category parent = categoryDAO.findById(c.getParentId());
                if (parent != null) {
                    parentName = parent.getName();
                }
            }
            
            Object[] row = {c.getId(), c.getName(), c.getSlug(), parentName};
            tableModel.addRow(row);
        }
    }
    
    private void loadComboBoxData() {
        cboParentCategory.removeAllItems();
        cboParentCategory.addItem(null);
        
        List<Category> categories = categoryDAO.findAll();
        for (Category c : categories) {
            cboParentCategory.addItem(c);
        }
    }
    
    private void loadSelectedRowToForm() {
        int selectedRow = tblCategories.getSelectedRow();
        if (selectedRow == -1) return;
        
        long categoryId = (Long) tableModel.getValueAt(selectedRow, 0);
        Category category = categoryDAO.findById(categoryId);
        
        if (category != null) {
            txtId.setText(String.valueOf(category.getId()));
            txtName.setText(category.getName());
            txtSlug.setText(category.getSlug());
            setParentCategorySelection(category.getParentId());
        }
    }
    
    private void setParentCategorySelection(Long parentId) {
        if (parentId == null) {
            cboParentCategory.setSelectedIndex(0);
            return;
        }
        
        for (int i = 0; i < cboParentCategory.getItemCount(); i++) {
            Category item = cboParentCategory.getItemAt(i);
            if (item != null && item.getId().equals(parentId)) {
                cboParentCategory.setSelectedIndex(i);
                return;
            }
        }
    }
    
    private void addCategory() {
        try {
            if (!validateForm()) return;
            
            Category category = new Category();
            category.setName(txtName.getText().trim());
            category.setSlug(txtSlug.getText().trim());
            
            Category selectedParent = (Category) cboParentCategory.getSelectedItem();
            if (selectedParent != null) {
                category.setParentId(selectedParent.getId());
            }
            
            boolean success = categoryDAO.insert(category);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Them danh muc thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Them danh muc that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCategory() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long chon danh muc can cap nhat!", "Canh bao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validateForm()) return;
            
            Category category = new Category();
            category.setId(Long.parseLong(txtId.getText().trim()));
            category.setName(txtName.getText().trim());
            category.setSlug(txtSlug.getText().trim());
            
            Category selectedParent = (Category) cboParentCategory.getSelectedItem();
            if (selectedParent != null) {
                if (selectedParent.getId().equals(category.getId())) {
                    JOptionPane.showMessageDialog(this, "Danh muc khong the la cha cua chinh no!", "Loi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                category.setParentId(selectedParent.getId());
            }
            
            boolean success = categoryDAO.update(category);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cap nhat danh muc thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Cap nhat danh muc that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCategory() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui long chon danh muc can xoa!", "Canh bao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Ban co chac chan muon xoa danh muc nay?", "Xac nhan xoa", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) return;
            
            long categoryId = Long.parseLong(txtId.getText().trim());
            boolean success = categoryDAO.delete(categoryId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Xoa danh muc thanh cong!", "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Xoa danh muc that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Loi: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtSlug.setText("");
        cboParentCategory.setSelectedIndex(0);
        tblCategories.clearSelection();
    }
    
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ten danh muc khong duoc de trong!", "Loi", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        
        if (txtSlug.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Slug khong duoc de trong!", "Loi", JOptionPane.WARNING_MESSAGE);
            txtSlug.requestFocus();
            return false;
        }
        
        return true;
    }
}
