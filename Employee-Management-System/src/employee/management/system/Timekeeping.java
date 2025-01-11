/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author taolam
 */

public class Timekeeping extends JFrame implements ActionListener {

    JTable table;
    Choice cTimekeepingId;
    JButton search, print, update, back;

    public Timekeeping() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel searchlbl = new JLabel("Search Timekeeping by User ID");
        searchlbl.setBounds(20, 20, 200, 20);
        add(searchlbl);

        cTimekeepingId = new Choice();
        cTimekeepingId.setBounds(220, 20, 150, 20);
        add(cTimekeepingId);

        // Fetch empIds for the Choice dropdown from the employee table
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM employee");
            while (rs.next()) {
                cTimekeepingId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 900, 600);
        add(jsp);
        
        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        update = new JButton("Update");
        update.setBounds(220, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        back = new JButton("Back");
        back.setBounds(320, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String empId = cTimekeepingId.getSelectedItem();
            String query = "SELECT date, start_time, end_time, " +
                           "TIMESTAMPDIFF(HOUR, start_time, end_time) AS hours_worked " +
                           "FROM timekeeping WHERE empId = ?";
            try {
                Conn c = new Conn();
                Connection con = c.getConnection(); // Lấy đối tượng Connection từ Conn
                PreparedStatement ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // Sử dụng Connection để tạo PreparedStatement
                ps.setString(1, empId);  // Thiết lập giá trị cho tham số '?' (empId)

                ResultSet rs = ps.executeQuery();

                // Dùng DbUtils để tạo TableModel từ ResultSet
                table.setModel(DbUtils.resultSetToTableModel(rs));

                // Tính toán tổng số ngày công và ngày phạt
                int totalDays = 0;
                int penaltyDays = 0;
                rs.beforeFirst();  // Reset con trỏ ResultSet về đầu để tính toán
                while (rs.next()) {
                    double hoursWorked = rs.getDouble("hours_worked");
                    if (hoursWorked >= 8) {
                        totalDays++; // Count as a valid day worked
                    } else if (hoursWorked > 0) {
                        penaltyDays++; // Count as a penalty day if hours worked < 8
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == update) {
            setVisible(false);
            UpdateTimekeeping updateFrame = new UpdateTimekeeping(); // Khởi tạo không tham số
            updateFrame.setEmpId(cTimekeepingId.getSelectedItem()); // Gọi setEmpId để truyền ID
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }
}