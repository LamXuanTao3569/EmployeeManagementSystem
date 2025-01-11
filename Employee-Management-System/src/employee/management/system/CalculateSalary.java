/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author taolam
 */
public class CalculateSalary extends JFrame implements ActionListener {
    Choice monthChoice, yearChoice;
    JButton viewButton, saveButton, back;
    JTable table;

    public CalculateSalary() {
        setLayout(null);

        JLabel heading = new JLabel("Calculate Salary");
        heading.setBounds(320, 20, 200, 30);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        add(heading);

        JLabel labelMonth = new JLabel("Month:");
        labelMonth.setBounds(50, 80, 50, 30);
        add(labelMonth);

        monthChoice = new Choice();
        monthChoice.setBounds(120, 80, 80, 30);
        for (int i = 1; i <= 12; i++) {
            monthChoice.add(String.valueOf(i));
        }
        add(monthChoice);

        JLabel labelYear = new JLabel("Year:");
        labelYear.setBounds(220, 80, 50, 30);
        add(labelYear);

        yearChoice = new Choice();
        yearChoice.setBounds(290, 80, 80, 30);
        for (int i = 2020; i <= 2030; i++) {
            yearChoice.add(String.valueOf(i));
        }
        add(yearChoice);

        viewButton = new JButton("View");
        viewButton.setBounds(390, 80, 80, 30);
        viewButton.addActionListener(this);
        add(viewButton);

        saveButton = new JButton("Save to Database");
        saveButton.setBounds(490, 80, 150, 30);
        saveButton.addActionListener(this);
        add(saveButton);

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 150, 880, 600);
        add(jsp);

        back = new JButton("Back");
        back.setBounds(660, 80, 100, 30);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        add(back);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == viewButton) {
            int month = Integer.parseInt(monthChoice.getSelectedItem());
            int year = Integer.parseInt(yearChoice.getSelectedItem());
            displaySalaryDetails(month, year);
        } else if (ae.getSource() == saveButton) {
            saveSalaryDetailsToDatabase();
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    private void displaySalaryDetails(int month, int year) {
        // Lấy danh sách nhân viên có bản ghi trong timekeeping
        String query = "SELECT e.empId, e.name, c.salary FROM employee e " +
                       "JOIN contract c ON e.empId = c.empId " +
                       "WHERE EXISTS (SELECT 1 FROM timekeeping t WHERE t.empId = e.empId AND MONTH(t.date) = ? AND YEAR(t.date) = ?)";
        String timekeepingQuery = "SELECT * FROM timekeeping WHERE empId = ? AND MONTH(date) = ? AND YEAR(date) = ?";

        try {
            Conn c = new Conn();
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Salary", "Days Worked", "Penalty", "Calculated Salary"}, 0);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            while (rs.next()) {
                String empId = rs.getString("empId");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");

                int daysWorked = 0;
                double penalty = 0;

                // Lấy dữ liệu từ bảng timekeeping
                PreparedStatement psTimekeeping = c.getConnection().prepareStatement(timekeepingQuery);
                psTimekeeping.setString(1, empId);
                psTimekeeping.setInt(2, month);
                psTimekeeping.setInt(3, year);
                ResultSet rsTimekeeping = psTimekeeping.executeQuery();

                while (rsTimekeeping.next()) {
                    Date startTime = timeFormat.parse(rsTimekeeping.getString("start_time"));
                    Date endTime = timeFormat.parse(rsTimekeeping.getString("end_time"));

                    long duration = endTime.getTime() - startTime.getTime();
                    long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

                    // Tính số ngày làm việc và phạt
                    if (diffInHours >= 8) {
                        daysWorked++; // Nếu làm đủ 8 giờ thì tính 1 ngày làm việc
                    } else if (diffInHours < 8) {
                        daysWorked++; // Ngày làm ít hơn 8 giờ cũng tính là 1 ngày làm việc
                        penalty += 500 * (8 - diffInHours); // Phạt 500 cho mỗi giờ thiếu
                    }
                }

                // Cập nhật công thức tính lương
                double calculatedSalary = (salary / 30) * daysWorked - penalty; // Điều chỉnh công thức
                calculatedSalary = Math.round(calculatedSalary);

                // Thêm dòng vào bảng
                model.addRow(new Object[]{empId, name, salary, daysWorked, penalty, calculatedSalary});
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSalaryDetailsToDatabase() {
        String query = "INSERT INTO salary (empId, work_day, penalty, calculated_salary, month, year) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Conn c = new Conn();
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                ps.setString(1, (String) model.getValueAt(i, 0));
                ps.setInt(2, Integer.parseInt(model.getValueAt(i, 3).toString()));
                ps.setDouble(3, Double.parseDouble(model.getValueAt(i, 4).toString()));
                ps.setDouble(4, Double.parseDouble(model.getValueAt(i, 5).toString()));
                ps.setInt(5, Integer.parseInt(monthChoice.getSelectedItem()));
                ps.setInt(6, Integer.parseInt(yearChoice.getSelectedItem()));

                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Salary details saved to database successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CalculateSalary();
    }
}