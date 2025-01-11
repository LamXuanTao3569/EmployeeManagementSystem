/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author taolam
 */

/**
 * AdminPanel: Chỉ có chức năng cập nhật role và xóa tài khoản.
 */
public class AdminPanel extends JFrame implements ActionListener {
    JTable userTable;
    JButton updateRole, deleteAccount, backButton;

    public AdminPanel() {
        setLayout(null);

        JLabel heading = new JLabel("Admin Panel: User Management");
        heading.setBounds(50, 20, 400, 30);
        add(heading);

        // Hiển thị danh sách tài khoản trong JTable
        String[] columnNames = {"ID", "Username", "Email", "Role"};
        userTable = new JTable();
        JScrollPane sp = new JScrollPane(userTable);
        sp.setBounds(50, 70, 500, 200);
        add(sp);

        // Tải dữ liệu vào bảng
        loadUserData(columnNames);

        updateRole = new JButton("Update Role");
        updateRole.setBounds(50, 300, 150, 30);
        updateRole.addActionListener(this);
        add(updateRole);

        deleteAccount = new JButton("Delete Account");
        deleteAccount.setBounds(250, 300, 150, 30);
        deleteAccount.addActionListener(this);
        add(deleteAccount);

        backButton = new JButton("Back");
        backButton.setBounds(450, 300, 100, 30);
        backButton.addActionListener(this);
        add(backButton);

        setSize(600, 400);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("Back")) {
            // Quay trở lại giao diện Home
            this.setVisible(false);
            new Home();
        } else {
            int row = userTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Please select a user!");
                return;
            }

            String username = userTable.getValueAt(row, 1).toString();

            try {
                Conn c = new Conn();
                if (action.equals("Update Role")) {
                    String newRole = JOptionPane.showInputDialog("Enter new role:");
                    if (newRole == null || newRole.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Role cannot be empty!");
                        return;
                    }

                    String query = "UPDATE login SET role=? WHERE username=?";
                    PreparedStatement ps = c.getConnection().prepareStatement(query);
                    ps.setString(1, newRole.trim());
                    ps.setString(2, username);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Role updated successfully!");
                } else if (action.equals("Delete Account")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?");
                    if (confirm == JOptionPane.YES_OPTION) {
                        String query = "DELETE FROM login WHERE username=?";
                        PreparedStatement ps = c.getConnection().prepareStatement(query);
                        ps.setString(1, username);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Account deleted successfully!");
                    }
                }
                // Refresh the table after any update
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            }
        }
    }

    // Hàm tải dữ liệu vào JTable
    private void loadUserData(String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        try {
            Conn c = new Conn();
            String query = "SELECT id, username, email, role FROM login";
            ResultSet rs = c.s.executeQuery(query);

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                model.addRow(new String[]{id, username, email, role});
            }
            userTable.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load user data.");
        }
    }

    // Hàm refresh bảng sau khi thực hiện thao tác
    private void refreshTable() {
        String[] columnNames = {"ID", "Username", "Email", "Role"};
        loadUserData(columnNames);
    }

    public static void main(String[] args) {
        new AdminPanel();
    }
}
