package crochetshop;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Manager {

    public static void managerDashboard() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;

        while(loop) {
            System.out.println("\n==============================================");
            System.out.println("       MANAGER DASHBOARD - CROCHET SHOP");
            System.out.println("==============================================");
            System.out.println(" [1] Add Provider User");
            System.out.println(" [2] Sell or Pull Out Product");
            System.out.println(" [3] Logout");
            System.out.println("==============================================");
            System.out.print(" Choose (1 - 3): ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch(choice) {
                case 1: 
                    addProviderUser(sc, db); 
                    break;
                case 2: 
                    manageProducts(sc, db); 
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

    private static void addProviderUser(Scanner sc, config db) {
        System.out.println("\n=== ADD PROVIDER USER ===");
        System.out.print("Enter Provider Fullname: ");
        String name = sc.nextLine();
        System.out.print("Enter Provider Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String hashedPassword = config.hashPassword(password);

        db.addRecord("INSERT INTO User (email, password, role, full_name) VALUES (?, ?, ?, ?)",
                     email, hashedPassword, "Provider",name);

        System.out.println("\nProvider user added successfully!");
    }

    private static void manageProducts(Scanner sc, config db) {
    boolean loop = true;

    while(loop) {
        System.out.println("\n=== PRODUCT MANAGEMENT ===");
        System.out.println(" [1] Sell Product");
        System.out.println(" [2] Pull Out Product");
        System.out.println(" [3] View Products");
        System.out.println(" [4] Back");
        System.out.print("Choose (1 - 4): ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {
            case 1: 
                sellProduct(sc, db); 
                break;
            case 2: 
                pullOutProduct(sc, db); 
                break;
            case 3:
                viewProducts(db);
                break;
            case 4: 
                loop = false; 
                break;
            default: 
                System.out.println("Invalid option!");
        }
    }
}
    private static void viewProducts(config db) {
        List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");

        if (products.isEmpty()) {
            System.out.println("No products available.");
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
    }


    private static void sellProduct(Scanner sc, config db) {
        List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");

        if (products.isEmpty()) {
            System.out.println("No products available to sell.");
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

        System.out.print("\nEnter Product ID to sell: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter quantity to sell: ");
        int qty = sc.nextInt();
        sc.nextLine();

        Map<String, Object> product = products.stream()
                         .filter(p -> ((int)p.get("product_id")) == id)
                         .findFirst()
                         .orElse(null);

        if (product == null) {
            System.out.println("Product ID not found!");
            return;
        }

        int stock = (int) product.get("stock");
        double price = (double) product.get("price");

        if (qty > stock) {
            System.out.println("Not enough stock available!");
            return;
        }

        int newStock = stock - qty;

        
        db.updateRecord("UPDATE Product SET stock = ? WHERE product_id = ?", newStock, id);

        
        double totalPrice = price * qty;
        db.addRecord(
            "INSERT INTO OrderTable (product_id, quantity, total_price) VALUES (?, ?, ?)",
            id, qty, totalPrice
        );

        System.out.println("Product sold successfully!");
}


    private static void pullOutProduct(Scanner sc, config db) {
        List<Map<String, Object>> products = db.fetchRecords("SELECT * FROM Product");

        if (products.isEmpty()) {
            System.out.println("No products available to remove.");
            return;
        }

        System.out.println("\n=== PRODUCT LIST ===");
        System.out.println("ID | Product Name | Price | Stock");
        for (Map<String, Object> p : products) {
            System.out.println(p.get("product_id") + " | " +
                               p.get("product_name") + " | ₱" +
                               p.get("price") + " | " +
                               p.get("stock"));
        }

        System.out.print("\nEnter Product ID to remove from inventory: ");
        int id = sc.nextInt();
        sc.nextLine();

        db.deleteRecord("DELETE FROM Product WHERE product_id = ?", id);
        System.out.println("Product removed successfully!");
    }
}
