package ui;

import dao.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Brand;
import models.Category;
import models.Product;
import models.ProductStatus;

public class ProductFrame extends JFrame {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final BrandDAO brandDAO;
    private final ProductStatusDAO statusDAO;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtSlug;
    private JTextArea txtDescription;
    private JTextField txtPrice;
    private JTextField txtAvailability;
    private JComboBox<Category> cboCategory;
    private JComboBox<Brand> cboBrand;
    private JComboBox<ProductStatus> cboStatus;

    private JTable tblProducts;
    private DefaultTableModel tableModel;

    private JTextField txtSearch;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;
    private JButton btnRefresh;

    public ProductFrame() {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAO();
        brandDAO = new BrandDAO();
        statusDAO = new ProductStatusDAO();

        setTitle("Quản lý Sản phẩm - Java Swing + JDBC");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();

        loadComboBoxData();
        loadTableData();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = createFormPanel();

        JPanel tablePanel = createTablePanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(400);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);

        btn.setOpaque(true);
        btn.setContentAreaFilled(true); // cho phép vẽ nền thật
        btn.setBorderPainted(false); // bỏ viền trắng khó chịu
        btn.setFocusPainted(false); // bỏ viền focus xanh

        return btn;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        // Form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 0: ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        fieldsPanel.add(txtId, gbc);

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(new JLabel("*Tên sản phẩm:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        fieldsPanel.add(txtName, gbc);

        // Row 2: Slug
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(new JLabel("*Slug:"), gbc);
        gbc.gridx = 1;
        txtSlug = new JTextField(20);
        fieldsPanel.add(txtSlug, gbc);

        // Row 3: Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldsPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        fieldsPanel.add(scrollDesc, gbc);

        // Row 4: Price
        gbc.gridx = 0;
        gbc.gridy = 4;
        fieldsPanel.add(new JLabel("*Giá:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(20);
        fieldsPanel.add(txtPrice, gbc);

        // Row 5: Availability
        gbc.gridx = 0;
        gbc.gridy = 5;
        fieldsPanel.add(new JLabel("*Số lượng:"), gbc);
        gbc.gridx = 1;
        txtAvailability = new JTextField(20);
        fieldsPanel.add(txtAvailability, gbc);

        // Row 6: Category
        gbc.gridx = 0;
        gbc.gridy = 6;
        fieldsPanel.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        cboCategory = new JComboBox<>();
        fieldsPanel.add(cboCategory, gbc);

        // Row 7: Brand
        gbc.gridx = 0;
        gbc.gridy = 7;
        fieldsPanel.add(new JLabel("Thương hiệu:"), gbc);
        gbc.gridx = 1;
        cboBrand = new JComboBox<>();
        fieldsPanel.add(cboBrand, gbc);

        // Row 8: Status
        gbc.gridx = 0;
        gbc.gridy = 8;
        fieldsPanel.add(new JLabel("*Trạng thái:"), gbc);
        gbc.gridx = 1;
        cboStatus = new JComboBox<>();
        fieldsPanel.add(cboStatus, gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = styleButton(new JButton("Thêm"), new Color(46, 204, 113));
        btnAdd.addActionListener(e -> addProduct());

        btnUpdate = styleButton(new JButton("Cập nhật"), new Color(52, 152, 219));
        btnUpdate.addActionListener(e -> updateProduct());

        btnDelete = styleButton(new JButton("Xóa"), new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteProduct());

        btnClear = styleButton(new JButton("Làm mới"), new Color(149, 165, 166));
        btnClear.addActionListener(e -> clearForm());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Tạo panel chứa bảng sản phẩm
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> searchProducts());
        searchPanel.add(btnSearch);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadTableData());
        searchPanel.add(btnRefresh);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Tên sản phẩm", "Giá", "Số lượng", "Danh mục", "Thương hiệu", "Trạng thái" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        tblProducts = new JTable(tableModel);
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProducts.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        tblProducts.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tblProducts.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        tblProducts.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
        tblProducts.getColumnModel().getColumn(3).setPreferredWidth(80); // Availability
        tblProducts.getColumnModel().getColumn(4).setPreferredWidth(120); // Category
        tblProducts.getColumnModel().getColumn(5).setPreferredWidth(120); // Brand
        tblProducts.getColumnModel().getColumn(6).setPreferredWidth(100); // Status

        // Row selection listener
        tblProducts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblProducts);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Load dữ liệu vào các ComboBox
     */
    private void loadComboBoxData() {
        // Load categories
        cboCategory.removeAllItems();
        cboCategory.addItem(null); // Option để không chọn
        List<Category> categories = categoryDAO.findAll();
        for (Category category : categories) {
            cboCategory.addItem(category);
        }

        // Load brands
        cboBrand.removeAllItems();
        cboBrand.addItem(null); // Option để không chọn
        List<Brand> brands = brandDAO.findAll();
        for (Brand brand : brands) {
            cboBrand.addItem(brand);
        }

        // Load status
        cboStatus.removeAllItems();
        List<ProductStatus> statusList = statusDAO.findAll();
        for (ProductStatus status : statusList) {
            cboStatus.addItem(status);
        }
    }

    /**
     * Load dữ liệu vào bảng
     */
    private void loadTableData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<Product> products = productDAO.findAll();
        for (Product product : products) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getAvailability(),
                    product.getCategoryName() != null ? product.getCategoryName() : "",
                    product.getBrandName() != null ? product.getBrandName() : "",
                    product.getStatusLabel()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Load dữ liệu từ dòng được chọn vào form
     */
    private void loadSelectedRowToForm() {
        int selectedRow = tblProducts.getSelectedRow();
        if (selectedRow == -1)
            return;

        long productId = (Long) tableModel.getValueAt(selectedRow, 0);
        Product product = productDAO.findById(productId);

        if (product != null) {
            txtId.setText(String.valueOf(product.getId()));
            txtName.setText(product.getName());
            txtSlug.setText(product.getSlug());
            txtDescription.setText(product.getDescription());
            txtPrice.setText(product.getPrice().toString());
            txtAvailability.setText(String.valueOf(product.getAvailability()));

            // Set ComboBox selections
            setComboBoxSelection(cboCategory, product.getCategoryId());
            setComboBoxSelection(cboBrand, product.getBrandId());
            setComboBoxSelection(cboStatus, product.getStatusId());
        }
    }

    /**
     * Set selected item trong ComboBox theo ID
     */
    private void setComboBoxSelection(JComboBox comboBox, Long id) {
        if (id == null) {
            comboBox.setSelectedIndex(0);
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object item = comboBox.getItemAt(i);
            if (item == null)
                continue;

            Long itemId = null;
            if (item instanceof Category) {
                itemId = ((Category) item).getId();
            } else if (item instanceof Brand) {
                itemId = ((Brand) item).getId();
            } else if (item instanceof ProductStatus) {
                itemId = ((ProductStatus) item).getId();
            }

            if (itemId != null && itemId.equals(id)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Thêm sản phẩm mới
     */
    private void addProduct() {
        try {
            // Validate
            if (!validateForm())
                return;

            // Tạo Product object
            Product product = new Product();
            product.setName(txtName.getText().trim());
            product.setSlug(txtSlug.getText().trim());
            product.setDescription(txtDescription.getText().trim());
            product.setPrice(new BigDecimal(txtPrice.getText().trim()));
            product.setAvailability(Integer.parseInt(txtAvailability.getText().trim()));

            Category selectedCategory = (Category) cboCategory.getSelectedItem();
            if (selectedCategory != null) {
                product.setCategoryId(selectedCategory.getId());
            }

            Brand selectedBrand = (Brand) cboBrand.getSelectedItem();
            if (selectedBrand != null) {
                product.setBrandId(selectedBrand.getId());
            }

            ProductStatus selectedStatus = (ProductStatus) cboStatus.getSelectedItem();
            product.setStatusId(selectedStatus.getId());

            // Insert vào DB
            boolean success = productDAO.insert(product);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Thêm sản phẩm thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm sản phẩm thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Giá và số lượng phải là số hợp lệ!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật sản phẩm
     */
    private void updateProduct() {
        try {
            // Kiểm tra đã chọn sản phẩm chưa
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn sản phẩm cần cập nhật!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate
            if (!validateForm())
                return;

            // Tạo Product object
            Product product = new Product();
            product.setId(Long.parseLong(txtId.getText().trim()));
            product.setName(txtName.getText().trim());
            product.setSlug(txtSlug.getText().trim());
            product.setDescription(txtDescription.getText().trim());
            product.setPrice(new BigDecimal(txtPrice.getText().trim()));
            product.setAvailability(Integer.parseInt(txtAvailability.getText().trim()));

            Category selectedCategory = (Category) cboCategory.getSelectedItem();
            if (selectedCategory != null) {
                product.setCategoryId(selectedCategory.getId());
            }

            Brand selectedBrand = (Brand) cboBrand.getSelectedItem();
            if (selectedBrand != null) {
                product.setBrandId(selectedBrand.getId());
            }

            ProductStatus selectedStatus = (ProductStatus) cboStatus.getSelectedItem();
            product.setStatusId(selectedStatus.getId());

            // Update trong DB
            boolean success = productDAO.update(product);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật sản phẩm thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật sản phẩm thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Giá và số lượng phải là số hợp lệ!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Xóa sản phẩm
     */
    private void deleteProduct() {
        try {
            // Kiểm tra đã chọn sản phẩm chưa
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn sản phẩm cần xóa!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa sản phẩm này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            long productId = Long.parseLong(txtId.getText().trim());
            boolean success = productDAO.delete(productId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Xóa sản phẩm thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa sản phẩm thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm sản phẩm
     */
    private void searchProducts() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }

        tableModel.setRowCount(0);
        List<Product> products = productDAO.searchByName(keyword);

        for (Product product : products) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getAvailability(),
                    product.getCategoryName() != null ? product.getCategoryName() : "",
                    product.getBrandName() != null ? product.getBrandName() : "",
                    product.getStatusLabel()
            };
            tableModel.addRow(row);
        }

        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy sản phẩm nào!",
                    "Kết quả tìm kiếm",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Làm mới form
     */
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtSlug.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtAvailability.setText("");
        cboCategory.setSelectedIndex(0);
        cboBrand.setSelectedIndex(0);
        if (cboStatus.getItemCount() > 0) {
            cboStatus.setSelectedIndex(0);
        }
        txtSearch.setText("");
        tblProducts.clearSelection();
    }

    /**
     * Validate form
     */
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tên sản phẩm không được để trống!",
                    "Lỗi validation",
                    JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return false;
        }

        if (txtSlug.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Slug không được để trống!",
                    "Lỗi validation",
                    JOptionPane.WARNING_MESSAGE);
            txtSlug.requestFocus();
            return false;
        }

        if (txtPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Giá không được để trống!",
                    "Lỗi validation",
                    JOptionPane.WARNING_MESSAGE);
            txtPrice.requestFocus();
            return false;
        }

        if (txtAvailability.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng không được để trống!",
                    "Lỗi validation",
                    JOptionPane.WARNING_MESSAGE);
            txtAvailability.requestFocus();
            return false;
        }

        if (cboStatus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn trạng thái!",
                    "Lỗi validation",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}
