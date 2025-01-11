package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class ManageContract extends JFrame implements ActionListener {
    JTable table;
    Choice ccontractId;
    JButton search, add, update, delete, print, back;

    public ManageContract() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel searchlbl = new JLabel("Search Contract by Id");
        searchlbl.setBounds(20, 20, 150, 20);
        add(searchlbl);

        ccontractId = new Choice();
        ccontractId.setBounds(180, 20, 150, 20);
        add(ccontractId);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from contract");
            while (rs.next()) {
                ccontractId.add(rs.getString("contractId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table = new JTable();
        loadTableData();

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 900, 600);
        add(jsp);

        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        add = new JButton("Add");
        add.setBounds(120, 70, 80, 20);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        update.setBounds(220, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        delete = new JButton("Delete");
        delete.setBounds(320, 70, 80, 20);
        delete.addActionListener(this);
        add(delete);

        print = new JButton("Print");
        print.setBounds(420, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        back = new JButton("Back");
        back.setBounds(520, 70, 80, 20);
        back.addActionListener(this);
        add(back);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    private void loadTableData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from contract");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateContractList() {
        ccontractId.removeAll();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from contract");
            while (rs.next()) {
                ccontractId.add(rs.getString("contractId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String query = "select * from contract where contractId = '" + ccontractId.getSelectedItem() + "'";
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == add) {
            setVisible(false);
            dispose();
            new AddContract();
        } else if (ae.getSource() == update) {
            setVisible(false);
            dispose();
            UpdateContract updateContract = new UpdateContract();
            updateContract.setContractId(ccontractId.getSelectedItem());
        } else if (ae.getSource() == delete) {
            try {
                int row = table.getSelectedRow(); // Lấy dòng được chọn trong bảng
                if (row != -1) {
                    String contractId = table.getValueAt(row, 0).toString(); // Giả sử contractId nằm ở cột đầu tiên
                    Conn c = new Conn();
                    String query = "delete from contract where contractId = '" + contractId + "'";
                    c.s.executeUpdate(query);
                    loadTableData();
                    JOptionPane.showMessageDialog(null, "Contract Deleted");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a contract to delete.");
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
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new ManageContract();
    }
}
