package employee.management.system;
import employee.management.system.GlobalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener{
    
    JTextField tfusername;
    JPasswordField tfpassword;
    
    Login() {
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(40, 20, 100, 30);
        add(lblusername);
        
        tfusername = new JTextField();
        tfusername.setBounds(150, 20, 150, 30);
        add(tfusername);
        
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(40, 70, 100, 30);
        add(lblpassword);
        
        tfpassword = new JPasswordField();
        tfpassword.setBounds(150, 70, 150, 30);
        add(tfpassword);
        
        JButton login = new JButton("LOGIN");
        login.setBounds(150, 140, 150, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);
        
        JButton register = new JButton("REGISTER");
        register.setBounds(150, 180, 150, 30);
        register.addActionListener(e -> {
            dispose(); // Đóng màn hình đăng nhập
            new Register(); // Mở giao diện đăng ký
        });
        add(register);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(350, 0, 200, 200);
        add(image);
        
        setSize(600, 300);
        setLocation(450, 200);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        try {
            String username = tfusername.getText();
            char[] passwordChars = tfpassword.getPassword(); // Lấy mật khẩu dưới dạng mảng char
            String password = new String(passwordChars);  // Chuyển mảng char thành chuỗi
            
            // Sử dụng PreparedStatement để ngăn ngừa SQL Injection
            Conn c = new Conn();
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                GlobalData.username = username; // Lưu username
                GlobalData.role = rs.getString("role").trim(); // Lưu role
                dispose(); // Ẩn cửa sổ đăng nhập
                new Home(); // Mở cửa sổ chính sau khi đăng nhập thành công
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        } catch (SQLException e) {
            // Xử lý lỗi kết nối cơ sở dữ liệu hoặc truy vấn
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi chung
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new Login();
    }
}
