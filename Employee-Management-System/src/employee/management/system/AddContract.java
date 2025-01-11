/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.sql.*;
/**
 *
 * @author taolam
 */
public class AddContract extends JFrame implements ActionListener {
    JTextField UserID, Salary, contractIdField;
    JDateChooser StartDate, EndDate;
    JComboBox<String> contractType, contractStatus, job;
    JComboBox<Integer> userIdComboBox;
    JButton add, back;

    public AddContract() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Add Contract Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        add(heading);

        addLabelAndField("User ID", 50, 100, 150, 30);
        userIdComboBox = addUserIdComboBox(200, 100, 150, 30);

        addLabelAndField("Salary", 400, 100, 150, 30);
        Salary = addTextField(600, 100, 150, 30);

        addLabelAndField("Start Date", 50, 150, 150, 30);
        StartDate = addDateChooser(200, 150, 150, 30);

        addLabelAndField("End Date", 400, 150, 150, 30);
        EndDate = addDateChooser(600, 150, 150, 30);
        
        // Thêm vào trong phần constructor AddContract:
        addLabelAndField("Contract ID", 50, 50, 150, 30);
        contractIdField = addTextField(200, 50, 150, 30); // Thêm trường contractId


        addLabelAndField("Contract Type", 50, 200, 150, 30);
        contractType = new JComboBox<>(new String[]{"Full-Time", "Part-Time", "Freelance"});
        contractType.setBounds(200, 200, 150, 30);
        contractType.setBackground(Color.WHITE);
        add(contractType);

        addLabelAndField("Contract Status", 400, 200, 150, 30);
        contractStatus = new JComboBox<>(new String[]{"Active", "Inactive", "Pending"});
        contractStatus.setBounds(600, 200, 150, 30);
        contractStatus.setBackground(Color.WHITE);
        add(contractStatus);

        addLabelAndField("Job", 50, 250, 150, 30);
        job = new JComboBox<>(new String[]{"Developer", "Designer", "Manager"});
        job.setBounds(200, 250, 150, 30);
        job.setBackground(Color.WHITE);
        add(job);

        add = addButton("Add Details", 250, 350, 150, 40);
        back = addButton("Back", 450, 350, 150, 40);

        setSize(900, 500);
        setLocation(300, 50);
        setVisible(true);
    }

    private JLabel addLabelAndField(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("serif", Font.PLAIN, 20));
        add(label);
        return label;
    }

    private JTextField addTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        add(textField);
        return textField;
    }

    private JDateChooser addDateChooser(int x, int y, int width, int height) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(x, y, width, height);
        add(dateChooser);
        return dateChooser;
    }

    private JButton addButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        add(button);
        return button;
    }
    
    private JComboBox<Integer> addUserIdComboBox(int x, int y, int width, int height) {
        JComboBox<Integer> userIdComboBox = new JComboBox<>();
        userIdComboBox.setBounds(x, y, width, height);
        userIdComboBox.setBackground(Color.WHITE);
        add(userIdComboBox);
        loadUserIds(userIdComboBox); // Tải danh sách userId từ cơ sở dữ liệu
        return userIdComboBox;
    }

    private void loadUserIds(JComboBox<Integer> userIdComboBox) {
        try {
            Conn conn = new Conn();
            String query = "SELECT empId FROM employee"; // Lấy danh sách empId từ bảng employee
            try (PreparedStatement ps = conn.getConnection().prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("empId");
                    userIdComboBox.addItem(userId); // Thêm userId vào JComboBox
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading user IDs: " + e.getMessage());
        }
    }

    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            if (addContractDetails()) {
                JOptionPane.showMessageDialog(null, "Details added successfully");
                setVisible(false);
                dispose();
                new ManageContract();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            dispose();
            new ManageContract();
        }
    }

    private boolean addContractDetails() {
    try {
        Conn conn = new Conn();
        int contractId = Integer.parseInt(contractIdField.getText()); // Lấy giá trị contractId từ trường nhập
        int userId = (int) userIdComboBox.getSelectedItem();
        double salary = Double.parseDouble(Salary.getText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(StartDate.getDate());
        String endDate = sdf.format(EndDate.getDate());
        String contractTypeValue = (String) contractType.getSelectedItem();
        String contractStatusValue = (String) contractStatus.getSelectedItem();
        String jobValue = (String) job.getSelectedItem();

        String checkUserQuery = "SELECT COUNT(*) FROM employee WHERE empId = ?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(checkUserQuery)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String query = "INSERT INTO contract (contractId, empId, salary, startDate, endDate, contractType, contractStatus, job, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
                    try (PreparedStatement pstmt = conn.getConnection().prepareStatement(query)) {
                        pstmt.setInt(1, contractId); // Gán giá trị cho contractId
                        pstmt.setInt(2, userId);
                        pstmt.setDouble(3, salary);
                        pstmt.setString(4, startDate);
                        pstmt.setString(5, endDate);
                        pstmt.setString(6, contractTypeValue);
                        pstmt.setString(7, contractStatusValue);
                        pstmt.setString(8, jobValue);
                        pstmt.executeUpdate();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User ID does not exist in the employee table.");
                    return false;
                }
            }
        }
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        return false;
    }
}


    public static void main(String[] args) {
        new AddContract();
    }
}
