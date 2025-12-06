package ui;

import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {
    
    public MainMenuFrame() {
        setTitle("He thong Quan ly San pham");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        
        JPanel headerPanel = new JPanel();
        JLabel lblTitle = new JLabel("HÊ THỐNG QUẢN LÝ SẢN PHẨM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 73, 94));
        headerPanel.add(lblTitle);
        
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton btnProducts = createMenuButton("Quản lý Sản phẩm", new Color(52, 152, 219));
        btnProducts.addActionListener(e -> openProductFrame());
        
        JButton btnCategories = createMenuButton("Quản lý Danh mục", new Color(46, 204, 113));
        btnCategories.addActionListener(e -> openCategoryFrame());
        
        JButton btnAttributes = createMenuButton("Quản lý Thuộc tính Sản phẩm", new Color(155, 89, 182));
        btnAttributes.addActionListener(e -> openAttributeFrame());
        
        JButton btnPriceHistory = createMenuButton("Xem lịch sử Giá", new Color(241, 196, 15));
        btnPriceHistory.addActionListener(e -> openPriceHistoryFrame());
        
        JButton btnExit = createMenuButton("Thoát", new Color(231, 76, 60));
        btnExit.addActionListener(e -> exitApplication());
        
        menuPanel.add(btnProducts);
        menuPanel.add(btnCategories);
        menuPanel.add(btnAttributes);
        menuPanel.add(btnPriceHistory);
        menuPanel.add(btnExit);
        
        // JPanel footerPanel = new JPanel();
        // // JLabel lblFooter = new JLabel("Java Swing + JDBC - Product Management System");
        // lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
        // lblFooter.setForeground(Color.GRAY);
        // footerPanel.add(lblFooter);
        
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        // add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void openProductFrame() {
        new ProductFrame();
    }
    
    private void openCategoryFrame() {
        new CategoryManagementFrame();
    }
    
    private void openAttributeFrame() {
        new ProductAttributeFrame();
    }
    
    private void openPriceHistoryFrame() {
        new PriceHistoryFrame();
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Ban co chac chan muon thoat khoi ung dung?",
            "Xac nhan thoat",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuFrame());
    }
}
