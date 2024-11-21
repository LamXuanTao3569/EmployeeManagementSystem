/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 *
 * @author taolam
 */
public class Register extends JFrame implements ActionListener {

    JTextField tfusername, tfemail;
    JPasswordField tfpassword, tfconfirmPassword;
    JButton register, back;

    Register() {
        setLayout(null);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(40, 20, 100, 30);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(150, 20, 150, 30);
        add(tfusername);

        JLabel lblemail = new JLabel("Email");
        lblemail.setBounds(40, 70, 100, 30);
        add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(150, 70, 150, 30);
        add(tfemail);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(40, 120, 100, 30);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(150, 120, 150, 30);
        add(tfpassword);

        JLabel lblconfirmPassword = new JLabel("Confirm Password");
        lblconfirmPassword.setBounds(40, 170, 150, 30);
        add(lblconfirmPassword);

        tfconfirmPassword = new JPasswordField();
        tfconfirmPassword.setBounds(150, 170, 150, 30);
        add(tfconfirmPassword);

        register = new JButton("REGISTER");
        register.setBounds(40, 230, 120, 30);
        register.addActionListener(this);
        add(register);

        back = new JButton("BACK");
        back.setBounds(180, 230, 120, 30);
        back.addActionListener(e -> {
            dispose();
            new Login(); // Quay lại màn hình đăng nhập
        });
        add(back);

        setSize(400, 350);
        setLocation(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = tfusername.getText();
        String email = tfemail.getText();
        String password = new String(tfpassword.getPassword());
        String confirmPassword = new String(tfconfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match!");
            return;
        }

        try {
            Conn c = new Conn();
            String query = "INSERT INTO login (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
                dispose();
                new Login(); // Chuyển về màn hình đăng nhập
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed! Try again.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
