/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
/**
 *
 * @author taolam
 */
public class UpdateContract extends JFrame implements ActionListener {
    JTextField tfsalary, tfstartDate, tfendDate, tfjobPosition;
    JButton update, back;
    JComboBox<String> cbContractId, cbContractType, cbContractStatus;

    public UpdateContract() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Update Contract Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        add(heading);

        JLabel selectContract = new JLabel("Select Contract ID");
        selectContract.setBounds(50, 100, 150, 30);
        selectContract.setFont(new Font("serif", Font.PLAIN, 20));
        add(selectContract);

        cbContractId = new JComboBox<>();
        cbContractId.setBounds(200, 100, 200, 30);
        add(cbContractId);

        // Load contract IDs into the combo box
        loadContractIDs();

        JLabel labelsalary = new JLabel("Salary");
        labelsalary.setBounds(50, 150, 150, 30);
        labelsalary.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelsalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(200, 150, 150, 30);
        add(tfsalary);

        JLabel labelstartDate = new JLabel("Start Date");
        labelstartDate.setBounds(400, 150, 150, 30);
        labelstartDate.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelstartDate);

        tfstartDate = new JTextField();
        tfstartDate.setBounds(600, 150, 150, 30);
        add(tfstartDate);

        JLabel labelendDate = new JLabel("End Date");
        labelendDate.setBounds(50, 200, 150, 30);
        labelendDate.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelendDate);

        tfendDate = new JTextField();
        tfendDate.setBounds(200, 200, 150, 30);
        add(tfendDate);

        JLabel labeljobPosition = new JLabel("Job Position");
        labeljobPosition.setBounds(400, 200, 150, 30);
        labeljobPosition.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeljobPosition);

        tfjobPosition = new JTextField();
        tfjobPosition.setBounds(600, 200, 150, 30);
        add(tfjobPosition);

        JLabel labelcontractType = new JLabel("Contract Type");
        labelcontractType.setBounds(50, 250, 150, 30);
        labelcontractType.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelcontractType);

        cbContractType = new JComboBox<>(new String[]{"Full-Time", "Part-Time", "Freelance"});
        cbContractType.setBounds(200, 250, 150, 30);
        cbContractType.setBackground(Color.WHITE);
        add(cbContractType);

        JLabel labelcontractStatus = new JLabel("Contract Status");
        labelcontractStatus.setBounds(400, 250, 150, 30);
        labelcontractStatus.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelcontractStatus);

        cbContractStatus = new JComboBox<>(new String[]{"Active", "Inactive", "Pending"});
        cbContractStatus.setBounds(600, 250, 150, 30);
        cbContractStatus.setBackground(Color.WHITE);
        add(cbContractStatus);

        cbContractId.addActionListener(e -> loadContractDetails());

        update = new JButton("Update Details");
        update.setBounds(250, 350, 150, 40);
        update.addActionListener(this);
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        add(update);

        back = new JButton("Back");
        back.setBounds(450, 350, 150, 40);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        add(back);

        setSize(900, 500);
        setLocation(300, 50);
        setVisible(true);
    }

    private void loadContractIDs() {
        try {
            Conn conn = new Conn();
            String query = "SELECT contractId FROM contract";
            ResultSet rs = conn.s.executeQuery(query);
            while (rs.next()) {
                cbContractId.addItem(rs.getString("contractId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContractDetails() {
        String contractId = (String) cbContractId.getSelectedItem();
        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM contract WHERE contractId = ?";
            try (PreparedStatement pstmt = conn.getConnection().prepareStatement(query)) {
                pstmt.setString(1, contractId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        tfsalary.setText(rs.getString("salary"));
                        tfstartDate.setText(rs.getString("startDate"));
                        tfendDate.setText(rs.getString("endDate"));
                        tfjobPosition.setText(rs.getString("job"));
                        cbContractType.setSelectedItem(rs.getString("contractType"));
                        cbContractStatus.setSelectedItem(rs.getString("contractStatus"));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading contract details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            updateContractDetails();
        } else if (ae.getSource() == back) {
            setVisible(false);
            dispose();
            new ManageContract();
        }
    }

    private void updateContractDetails() {
        String contractId = (String) cbContractId.getSelectedItem();
        String salary = tfsalary.getText();
        String startDate = tfstartDate.getText();
        String endDate = tfendDate.getText();
        String job = tfjobPosition.getText();
        String contractTypeValue = (String) cbContractType.getSelectedItem();
        String contractStatusValue = (String) cbContractStatus.getSelectedItem();

        try {
            Conn conn = new Conn();
            String query = "UPDATE contract SET salary = ?, startDate = ?, endDate = ?, job = ?, contractType = ?, contractStatus = ? WHERE contractId = ?";
            try (PreparedStatement pstmt = conn.getConnection().prepareStatement(query)) {
                pstmt.setDouble(1, Double.parseDouble(salary));
                pstmt.setDate(2, Date.valueOf(startDate));
                pstmt.setDate(3, Date.valueOf(endDate));
                pstmt.setString(4, job);
                pstmt.setString(5, contractTypeValue);
                pstmt.setString(6, contractStatusValue);
                pstmt.setString(7, contractId);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Details updated successfully");
                setVisible(false);
                new ManageContract();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating contract details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setContractId(String contractId) {
        this.cbContractId.setSelectedItem(contractId);
        loadContractDetails();
    }
    
    public static void main(String[] args) {
        new UpdateContract();
    }
}