package employee.management.system;

import java.sql.*;

public class Conn {
    
    Connection c;
    Statement s;

    public Conn () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:///employeemanagementsystem", "root", "1234567890Abc@");
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
            return c;
    }
     
    
    // Thêm phương thức close để đóng kết nối khi không cần thiết
    public void close() throws SQLException {
        if (s != null) {
            s.close();
        }
        if (c != null) {
            c.close();
        }
    }    
}
