/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

import java.awt.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author taolam
 */
public class UpdateTimekeeping extends JFrame implements ActionListener {
    
    JLabel lblempId, lblName;
    JDateChooser tfdate;
    JSpinner spStartTime, spEndTime;
    JButton add, back;
    String empId;

    public UpdateTimekeeping() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Update Timekeeping Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        add(heading);

        JLabel lblempIdLabel = new JLabel("Employee ID:");
        lblempIdLabel.setBounds(50, 100, 150, 30);
        lblempIdLabel.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblempIdLabel);

        lblempId = new JLabel();
        lblempId.setBounds(200, 100, 150, 30);
        lblempId.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblempId);

        JLabel lblNameLabel = new JLabel("Name:");
        lblNameLabel.setBounds(400, 100, 150, 30);
        lblNameLabel.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblNameLabel);

        lblName = new JLabel();
        lblName.setBounds(550, 100, 200, 30);
        lblName.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblName);

        JLabel labeldate = new JLabel("Date");
        labeldate.setBounds(50, 200, 150, 30);
        labeldate.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeldate);

        tfdate = new JDateChooser();
        tfdate.setBounds(200, 200, 150, 30);
        add(tfdate);

        JLabel labelstart_time = new JLabel("Start time");
        labelstart_time.setBounds(400, 200, 150, 30);
        labelstart_time.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelstart_time);

        spStartTime = createTimeSpinner();
        spStartTime.setBounds(550, 200, 150, 30);
        add(spStartTime);

        JLabel labelend_time = new JLabel("End time");
        labelend_time.setBounds(50, 250, 150, 30);
        labelend_time.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelend_time);

        spEndTime = createTimeSpinner();
        spEndTime.setBounds(200, 250, 150, 30);
        add(spEndTime);

        add = new JButton("Update Details");
        add.setBounds(250, 350, 150, 40);
        add.addActionListener(this);
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add(add);

        back = new JButton("Back");
        back.setBounds(450, 350, 150, 40);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        add(back);

        setSize(900, 500);
        setLocation(300, 100);
        setVisible(true);
    }

    private JSpinner createTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        spinner.setEditor(editor);
        return spinner;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            if (empId == null || empId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Employee ID not set. Please select an employee.");
                return;
            }

            Date date = tfdate.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(date);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String start_time = timeFormat.format(spStartTime.getValue());
            String end_time = timeFormat.format(spEndTime.getValue());

            try {
                Conn conn = new Conn();
                String query = "INSERT INTO timekeeping (empId, date, start_time, end_time) VALUES ('" + empId + "', '" + dateString + "', '" + start_time + "', '" + end_time + "')";
                conn.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Details updated successfully");
                setVisible(false);
                new Timekeeping();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Timekeeping();
        }
    }

    public void setEmpId(String empId) {
        this.empId = empId;
        lblempId.setText(empId);
        try {
            Conn c = new Conn();
            String query = "SELECT e.empId, e.name, t.date, t.start_time, t.end_time FROM employee e LEFT JOIN timekeeping t ON e.empId = t.empId WHERE e.empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            if (rs.next()) {
                lblName.setText(rs.getString("name"));
                tfdate.setDate(rs.getDate("date"));

                Time startTime = rs.getTime("start_time");
                if (startTime != null) {
                    spStartTime.setValue(startTime);
                }

                Time endTime = rs.getTime("end_time");
                if (endTime != null) {
                    spEndTime.setValue(endTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UpdateTimekeeping frame = new UpdateTimekeeping();
        frame.setEmpId("E101"); // Example usage of setting empId
    }
}