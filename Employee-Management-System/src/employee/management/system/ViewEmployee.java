package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class ViewEmployee extends JFrame implements ActionListener{

    JTable table;
    JComboBox<String> cemployeeId;
    JButton search, print, update, back;
    
    ViewEmployee() {
        
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel searchlbl = new JLabel("Search by Employee Id");
        searchlbl.setBounds(20, 20, 150, 20);
        add(searchlbl);
        
        cemployeeId = new JComboBox<>();
        cemployeeId.setBounds(180, 20, 150, 20);
        add(cemployeeId);
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select empId from employee");
            while(rs.next()) {
                cemployeeId.addItem(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        table = new JTable();
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from employee");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
            String empId = cemployeeId.getSelectedItem().toString();
            String query = "SELECT * FROM employee WHERE empId = ?";
            try {
                Conn c = new Conn();
                PreparedStatement ps = c.getConnection().prepareStatement(query);
                ps.setString(1, empId);  // Sử dụng PreparedStatement để tránh SQL Injection
                ResultSet rs = ps.executeQuery();
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while searching: " + e.getMessage());
            }
        } else if (ae.getSource() == print) {
            try {
                boolean printed = table.print();
                if (!printed) {
                    JOptionPane.showMessageDialog(null, "Printing failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while printing: " + e.getMessage());
            }
        } else if (ae.getSource() == update) {
            int response = JOptionPane.showConfirmDialog(null, "Do you want to update this employee?", "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                setVisible(false);
                UpdateEmployee updateEmployee = new UpdateEmployee();
                updateEmployee.setEmployeeId(cemployeeId.getSelectedItem().toString());

            }
        } else if (ae.getSource() == back) {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to go back?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                setVisible(false);
                new Home();
            }
        }
    }

    public static void main(String[] args) {
        new ViewEmployee();
    }
}
