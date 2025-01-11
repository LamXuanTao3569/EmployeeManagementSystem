/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package employee.management.system;

/**
 *
 * @author taolam
 */
public class GlobalData {
        // Biến lưu trữ thông tin người dùng
    public static String username; 
    public static String role;

    // Phương thức reset thông tin (nếu cần thiết)
    public static void clear() {
        username = null;
        role = null;
    }
}
