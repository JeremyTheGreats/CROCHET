/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crochetshop;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author JeremyTheGreats
 */
public class Owner {
    
    
    public static void Owner(){
        
        Scanner sc = new Scanner(System.in);
   
        boolean loop = true;
    
    while(loop){
        System.out.println("\n==============================================");
        System.out.println("         OWNER DASHBOARD - CROCHET SHOP");
        System.out.println("==============================================");
        System.out.println(" [1] Manage Products");
        System.out.println(" [2] Manage Users");
        System.out.println(" [3] View Orders");
        System.out.println(" [4] Logout");
        System.out.println("==============================================");
        System.out.print(" Choose (1 - 4): ");

        int choose = sc.nextInt();
        sc.nextLine();
        
        switch (choose) {
                
                case 1:
                    Manageproduct();
                    break;

                case 2:
                    manageUsers();
                    break;

                case 3:
                    viewOrders();
                    break;

                case 4:
                    System.out.println("\nLogging out...");
                    
                    loop = false;
                    break;

                default:
                    System.out.println("\nInvalid choice. Try again.");
            }
    }
    
    }
    
    private static void Manageproduct(){
        
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;
        
        while (loop) {
            
            System.out.println("\n=== PRODUCT MANAGEMENT ===");
            System.out.println(" [1] Add Product");
            System.out.println(" [2] View Products");
            System.out.println(" [3] Delete Product");
            System.out.println(" [4] Back");
            System.out.print(" Choose (1 - 4): ");
            
            int choose = sc.nextInt();
            sc.nextLine();

            switch (choose) {
                
                case 1:
                    
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter stock quantity: ");
                    int stock = sc.nextInt();
                    sc.nextLine();

                    String sql = "INSERT INTO Product (product_name, price, stock) VALUES (?, ?, ?)";
                    db.addRecord(sql, name, price, stock);
                    System.out.println("\nProduct added successfully!");
                    
                    break;

                case 2:
                    
                    System.out.println("\n=== PRODUCT LIST ===");
                    
                    List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");
                    
                        if (products.isEmpty()) {
                            System.out.println("No products found.");
                        } 
                        else {
                            
                        for (Map<String, Object> p : products) {
                            System.out.println("ID: " + p.get("product_id") +
                                    " | Name: " + p.get("product_name") +
                                    " | Price: ₱" + p.get("price") +
                                    " | Stock: " + p.get("stock"));
                        }
                    }
                        
                    break;

                case 3:
                    
                    System.out.print("Enter Product ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    
                    db.deleteRecord("DELETE FROM Product WHERE product_id = ?", id);
                    
                    System.out.println("\nProduct deleted successfully!");
                    
                    break;

                case 4:
                    
                    loop = false;
                    
                    break;

                default:
                    
                    System.out.println("Invalid option!");
            }
        }
        
        
    }
    private static void manageUsers() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;
        
        while (loop) {
            
            System.out.println("\n=== USER MANAGEMENT ===");
            System.out.println(" [1] View Users");
            System.out.println(" [2] Delete User");
            System.out.println(" [3] Back");
            System.out.print(" Choose (1 - 3): ");
            
            int choose = sc.nextInt();
            sc.nextLine();

            switch (choose) {
                case 1:
                    
                    List<Map<String, Object>> users = db.fetchRecords("SELECT * FROM User");
                    
                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } 
                        else {
                            
                        System.out.println("\n=== USER LIST ===");
                        
                        for (Map<String, Object> u : users) {
                            System.out.println("ID: " + u.get("user_id") +
                                    " | Email: " + u.get("email") +
                                    " | Role: " + u.get("role"));
                        }
                    }
                        
                    break;

                case 2:
                    
                    System.out.print("Enter User ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    
                    db.deleteRecord("DELETE FROM User WHERE user_id = ?", id);
                    System.out.println("\nUser deleted successfully!");
                    break;

                case 3:
                    
                    loop = false;
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void viewOrders() {
        config db = new config();
        
        System.out.println("\n=== ORDER LIST ===");
        List<Map<String, Object>> orders = db.fetchRecords("SELECT * FROM OrderTable");
        if (orders.isEmpty()) {
            System.out.println("No orders yet.");
        } else {
            for (Map<String, Object> o : orders) {
                System.out.println("Order ID: " + o.get("order_id") +
                        " | Product ID: " + o.get("product_id") +
                        " | Quantity: " + o.get("quantity") +
                        " | Total: ₱" + o.get("total_price") +
                        " | Date: " + o.get("order_date"));
            }
        }
    }

}
