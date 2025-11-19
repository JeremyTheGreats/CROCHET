
package crochetshop;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Manager {
    
    public static void Manager(){
        
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        
        while (loop) {
            System.out.println("\n==============================================");
            System.out.println("         MANAGER DASHBOARD - CROCHET SHOP");
            System.out.println("==============================================");
            System.out.println(" [1] Manage Products");
            System.out.println(" [2] View Orders");
            System.out.println(" [3] Logout");
            System.out.println("==============================================");
            System.out.print(" Choose (1 - 3): ");

            int choose = sc.nextInt();
            sc.nextLine();

            switch (choose) {
                case 1:
                    manageProducts();
                    break;

                case 2:
                    viewOrders();
                    break;

                case 3:
                    System.out.println("\nLogging out...");
                    loop = false;
                    break;

                default:
                    System.out.println("\nInvalid choice. Try again.");
            }
        }
        
    }
    
    private static void manageProducts() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;
        
        while (loop) {
            System.out.println("\n=== PRODUCT MANAGEMENT ===");
            System.out.println(" [1] Add Product");
            System.out.println(" [2] View Products");
            System.out.println(" [3] Update Stock");
            System.out.println(" [4] Back");
            System.out.print(" Choose (1 - 4): ");
            String choose = sc.nextLine();
            

            switch (choose) {
                
                case "1":
                    boolean addMore = true;

                    while (addMore) {
                        
                        System.out.println("\n=== ADD PRODUCT ===");

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

                        System.out.print("\nAdd another product? (Y/N): ");
                        String again = sc.nextLine().trim().toUpperCase();

                        if (!again.equals("Y")) {  
                            addMore = false;
                        }
                    }

                break;


                case "2":
                    System.out.println("\n=== PRODUCT LIST ===");
                    List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");
                    if (products.isEmpty()) {
                        System.out.println("No products found.");
                    } else {
                        System.out.println("---------------------------------------------------------------");
                        System.out.println("| ID  | Product Name           | Price        | Stock        |");
                        System.out.println("---------------------------------------------------------------");

                        for (Map<String, Object> p : products) {
                            System.out.printf(
                                "| %-3s | %-23s | ₱%-11s | %-11s |\n",
                                p.get("product_id"),
                                p.get("product_name"),
                                p.get("price"),
                                p.get("stock")
                            );
                        }

                        System.out.println("---------------------------------------------------------------");

                    }
                    break;

                case "3":
                    System.out.print("Enter Product ID to update stock: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new stock quantity: ");
                    int newStock = sc.nextInt();
                    sc.nextLine();

                    db.updateRecord("UPDATE Product SET stock = ? WHERE product_id = ?", newStock, id);
                    System.out.println("\nStock updated successfully!");
                    break;

                case "4":
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
