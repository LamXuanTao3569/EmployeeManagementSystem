package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 

public class Home extends JFrame implements ActionListener{

    JButton view, add, update, remove;
    
    // Phương thức kiểm tra người dùng đã đăng nhập chưa
    private static boolean checkIfLoggedIn() {
        // Kiểm tra thông qua một biến toàn cục, cơ sở dữ liệu, hoặc tệp cấu hình
        return false; 
    }
    
    Home() { 
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
        
        update = new JButton("Update Employee");
        update.setBounds(650, 140, 150, 40);
        update.addActionListener(this);
        image.add(update);
        
        remove = new JButton("Remove Employee");
        remove.setBounds(820, 140, 150, 40);
        remove.addActionListener(this);
        image.add(remove);
        
        // Cải tiến giao diện - Thêm hiệu ứng hover cho các nút
        addMouseEffect(add);
        addMouseEffect(view);
        addMouseEffect(update);
        addMouseEffect(remove);
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Home Page");
        welcomeLabel.setBounds(200, 100, 300, 30);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(welcomeLabel);
        
        JButton logout = new JButton("LOGOUT");
        logout.setBounds(500, 240, 150, 40);
        logout.addActionListener(e -> {
            setVisible(false);
            new Login();
        });
        add(logout);
        
                
        setSize(1120, 630);
        setLocation(250, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    // Hàm xử lý các sự kiện
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if (source == add) {
            openWindow(new AddEmployee());
        } else if (source == view) {
            openWindow(new ViewEmployee());
        } else if (source == update) {
            openWindow(new UpdateEmployee("")); // Mở màn hình UpdateEmployee thay vì ViewEmployee
        } else if (source == remove) {
            openWindow(new RemoveEmployee());
        }
    }
    
    // Hàm mở cửa sổ mới
    private void openWindow(JFrame newWindow) {
        setVisible(false);
        newWindow.setVisible(true);
    }
    
    // Thêm hiệu ứng hover cho nút
    private void addMouseEffect(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY); // Khi di chuột vào nút
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK); // Phục hồi màu khi chuột ra khỏi nút
            }
        });
    }

    public static void main(String[] args) {
        if (checkIfLoggedIn()) {
            new Login();
        } else {
            new Home();
        }
    }   
}
