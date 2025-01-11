package employee.management.system;
import employee.management.system.GlobalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Home extends JFrame implements ActionListener{

    JButton view, add, remove, logout, contract, adminPanel, timekeeping, salaryCalculate;
    String username, role;

    private static boolean checkIfLoggedIn() {
        return true; // Giả sử người dùng đã đăng nhập
    }

    private String fetchUserRole(String username) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be null or empty.");
            return null;
        }

        String role = "";
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            Conn c = new Conn();
            String query = "SELECT role FROM login WHERE username = ?";
            ps = c.getConnection().prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                role = rs.getString("role").trim();
            } else {
                JOptionPane.showMessageDialog(null, "No role found for username: " + username);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while fetching role: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return role.isEmpty() ? "Guest" : role;
    }

    public Home() {
        this.username = GlobalData.username;
        this.role = GlobalData.role;

        if (username == null || username.isEmpty() || role == null || role.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Failed to load user information.");
            return;
        }

        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/home.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1120, 630, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1120, 630);
        add(image);

        JLabel heading = new JLabel("Employee Management System");
        heading.setBounds(620, 20, 400, 40);
        heading.setFont(new Font("Raleway", Font.BOLD, 25));
        image.add(heading);

        add = new JButton("Add Employee");
        add.setBounds(650, 80, 150, 40);
        add.addActionListener(this);
        image.add(add);

        view = new JButton("View Employees");
        view.setBounds(820, 80, 150, 40);
        view.addActionListener(this);
        image.add(view);

        remove = new JButton("Remove Employee");
        remove.setBounds(650, 140, 150, 40);
        remove.addActionListener(this);
        image.add(remove);

        contract = new JButton("Contract");
        contract.setBounds(820, 140, 150, 40);
        contract.addActionListener(this);
        image.add(contract);

        adminPanel = new JButton("Admin Panel");
        adminPanel.setBounds(650, 200, 150, 40);
        adminPanel.addActionListener(this);
        image.add(adminPanel);

        timekeeping = new JButton("Timekeeping");
        timekeeping.setBounds(820, 200, 150, 40);
        timekeeping.addActionListener(this);
        image.add(timekeeping);

        salaryCalculate = new JButton("Salary Calculate");
        salaryCalculate.setBounds(650, 260, 150, 40);
        salaryCalculate.addActionListener(this);
        image.add(salaryCalculate);

        addMouseEffect(add);
        addMouseEffect(view);
        addMouseEffect(remove);
        addMouseEffect(contract);
        addMouseEffect(adminPanel);
        addMouseEffect(timekeeping);
        addMouseEffect(salaryCalculate);

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(200, 100, 300, 30);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(welcomeLabel);

        logout = new JButton("LOGOUT");
        logout.setBounds(500, 480, 150, 40);
        logout.addActionListener(this);
        image.add(logout);

        setSize(1120, 630);
        setLocation(250, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if (source == add) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("TPNS")) {
                openWindow(new AddEmployee());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == view) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("TPNS") || role.equalsIgnoreCase("KT")) {
                openWindow(new ViewEmployee());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == remove) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("TPNS")) {
                openWindow(new RemoveEmployee());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == contract) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("TPNS")) {
                openWindow(new ManageContract());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == adminPanel) {
            if (role.equalsIgnoreCase("Admin")) {
                openWindow(new AdminPanel());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == timekeeping) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("KT")) {
                openWindow(new Timekeeping());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == salaryCalculate) {
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("TPNS") || role.equalsIgnoreCase("KT")) {
                openWindow(new CalculateSalary());
            } else {
                showAccessDeniedMessage();
            }
        } else if (source == logout) {
            openWindow(new Login());
        }
    }

    private void openWindow(JFrame newWindow) {
        setVisible(false);
        newWindow.setVisible(true);
    }

    private void addMouseEffect(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }
        });
    }

    private void showAccessDeniedMessage() {
        JOptionPane.showMessageDialog(null, "You do not have permission to access this feature.");
    }

    public static void main(String[] args) {
        if (checkIfLoggedIn()) {
            new Login();
        } else {
            new Home();
        }
    }
}