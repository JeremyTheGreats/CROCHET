package crochetshop;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Provider {

    public static void providerDashboard() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;

        while(loop) {
            System.out.println("\n==============================================");
            System.out.println("        PROVIDER DASHBOARD - CROCHET SHOP");
            System.out.println("==============================================");
            System.out.println(" [1] Add Product");
            System.out.println(" [2] Update Product");
            System.out.println(" [3] Logout");
            System.out.println("==============================================");
            System.out.print("Choose (1 - 3): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1: 
                    addProduct(sc, db); 
                    break;
                case 2: 
                    updateProduct(sc, db); 
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

    private static void addProduct(Scanner sc, config db) {
        System.out.println("\n=== ADD PRODUCT ===");

        System.out.print("Enter product name: ");
        String name = sc.nextLine();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter stock quantity: ");
        int stock = sc.nextInt();
        sc.nextLine();

        db.addRecord("INSERT INTO Product (product_name, price, stock) VALUES (?, ?, ?)",
                     name, price, stock);

        System.out.println("\nProduct added successfully!");
    }

    private static void updateProduct(Scanner sc, config db) {
        List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");

        if (products.isEmpty()) {
            System.out.println("No products available to update.");
            return;
        }

        System.out.println("\n=== PRODUCT LIST ===");
        System.out.printf("%-10s %-20s %-10s %-10s\n", "ID", "Product Name", "Price", "Stock");
        System.out.println("---------------------------------------------------------");

        for (Map<String, Object> p : products) {
            System.out.printf(
                "%-10s %-20s ₱%-9.2f %-10s\n",
                p.get("product_id"),
                p.get("product_name"),
                p.get("price"),
                p.get("stock")
            );
        }

        System.out.print("\nEnter Product ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new product name: ");
        String name = sc.nextLine();

        System.out.print("Enter new price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter new stock quantity: ");
        int stock = sc.nextInt();
        sc.nextLine();

        db.updateRecord("UPDATE Product SET product_name = ?, price = ?, stock = ? WHERE product_id = ?",
                        name, price, stock, id);

        System.out.println("\nProduct updated successfully!");
    }
}
