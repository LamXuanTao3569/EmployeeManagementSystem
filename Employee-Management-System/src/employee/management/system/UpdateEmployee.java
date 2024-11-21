package employee.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateEmployee extends JFrame implements ActionListener{
    
    JTextField tfeducation, tfname, tffname, tfaddress, tfphone, tfccid, tfemail, tfsalary, tfdesignation, tfdob, tfempId;
    JLabel lblempId;
    JButton add, back;
    String empId;
    JComboBox<String> empIdComboBox;

    
    UpdateEmployee(String empId) {
        this.empId = empId;
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel heading = new JLabel("Update Employee Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        add(heading);
        
        JLabel labelname = new JLabel("Name");
        labelname.setBounds(50, 150, 150, 30);
        labelname.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelname);
        
        tfname = new JTextField();
        tfname.setBounds(200, 150, 150, 30);
        add(tfname);
        
        JLabel labelfname = new JLabel("Father's Name");
        labelfname.setBounds(400, 150, 150, 30);
        labelfname.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelfname);
        
        tffname = new JTextField();
        tffname.setBounds(600, 150, 150, 30);
        add(tffname);
        
        JLabel labeldob = new JLabel("Date of Birth");
        labeldob.setBounds(50, 200, 150, 30);
        labeldob.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeldob);
        
        tfdob = new JTextField();
        tfdob.setBounds(200, 200, 150, 30);
        add(tfdob);
        
        JLabel labelsalary = new JLabel("Salary");
        labelsalary.setBounds(400, 200, 150, 30);
        labelsalary.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelsalary);
        
        tfsalary = new JTextField();
        tfsalary.setBounds(600, 200, 150, 30);
        add(tfsalary);
        
        JLabel labeladdress = new JLabel("Address");
        labeladdress.setBounds(50, 250, 150, 30);
        labeladdress.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeladdress);
        
        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        add(tfaddress);
        
        JLabel labelphone = new JLabel("Phone");
        labelphone.setBounds(400, 250, 150, 30);
        labelphone.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelphone);
        
        tfphone = new JTextField();
        tfphone.setBounds(600, 250, 150, 30);
        add(tfphone);
        
        JLabel labelemail = new JLabel("Email");
        labelemail.setBounds(50, 300, 150, 30);
        labelemail.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelemail);
        
        tfemail = new JTextField();
        tfemail.setBounds(200, 300, 150, 30);
        add(tfemail);
        
        JLabel labeleducation = new JLabel("Higest Education");
        labeleducation.setBounds(400, 300, 150, 30);
        labeleducation.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeleducation);
        
        tfeducation = new JTextField();
        tfeducation.setBounds(600, 300, 150, 30);
        add(tfeducation);
        
        JLabel labeldesignation = new JLabel("Designation");
        labeldesignation.setBounds(50, 350, 150, 30);
        labeldesignation.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeldesignation);
        
        tfdesignation = new JTextField();
        tfdesignation.setBounds(200, 350, 150, 30);
        add(tfdesignation);
        
        JLabel labelccid= new JLabel("CCID Number");
        labelccid.setBounds(400, 350, 150, 30);
        labelccid.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelccid);
        
        tfccid = new JTextField();
        tfccid.setBounds(600, 350, 150, 30);
        add(tfccid);
        
        JLabel labelempId = new JLabel("Employee id");
        labelempId.setBounds(50, 400, 150, 30);
        labelempId.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelempId);
        
        empIdComboBox = new JComboBox<>();
        empIdComboBox.setBounds(200, 400, 150, 30);
        add(empIdComboBox);
        
        tfempId = new JTextField();
        tfempId.setBounds(200, 400, 150, 30);
        add(tfempId);  // Đảm bảo thêm vào layout

                // Load Employee IDs into ComboBox
        try {
            Conn c = new Conn();
            String query = "SELECT empId FROM employee";
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                empIdComboBox.addItem(rs.getString("empId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
        
        // Add ActionListener to fetch employee details when an ID is selected
        empIdComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedEmpId = (String) empIdComboBox.getSelectedItem();
                displayEmployeeInfo(selectedEmpId);
            }
        });
        
 /*       try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = ?";
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfccid.setText(rs.getString("ccid"));
                lblempId.setText(rs.getString("empId"));
                tfdesignation.setText(rs.getString("designation"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        } */
        
        add = new JButton("Update Details");
        add.setBounds(250, 550, 150, 40);
        add.addActionListener(this);
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add(add);
        
        back = new JButton("Back");
        back.setBounds(450, 550, 150, 40);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        add(back);
        
        setSize(900, 700);
        setLocation(300, 50);
        setVisible(true);
    }
    
        // Method to display employee info
    private void displayEmployeeInfo(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = ?";
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tfempId.setText(rs.getString("empId"));
                tffname.setText(rs.getString("fname"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfsalary.setText(rs.getString("salary"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfccid.setText(rs.getString("ccid"));
                tfdesignation.setText(rs.getString("designation"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String empId = tfempId.getText();  // Employee ID cannot be edited
            String name = tfname.getText();
            String dob = tfdob.getText();
            String fname = tffname.getText();
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = tfeducation.getText();
            String designation = tfdesignation.getText();
            String ccid = tfccid.getText();

            try {
                Conn conn = new Conn();
                String query = "UPDATE employee SET name = ?, dob = ?, fname = ?, salary = ?, address = ?, phone = ?, email = ?, education = ?, designation = ?, ccid = ? WHERE empId = ?";
                PreparedStatement ps = conn.getConnection().prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, dob);
                ps.setString(3, fname);
                ps.setString(4, salary);
                ps.setString(5, address);
                ps.setString(6, phone);
                ps.setString(7, email);
                ps.setString(8, education);
                ps.setString(9, designation);
                ps.setString(10, ccid);
                ps.setString(11, empId);  // Keep Employee ID unchanged
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Details updated successfully");
                setVisible(false);
                new Home();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating data: " + e.getMessage());
            }
        } else {
            setVisible(false);
            new Home();
        } 
    }

    public static void main(String[] args) {
        new UpdateEmployee("");
    }
}
