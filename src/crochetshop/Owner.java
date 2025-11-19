package crochetshop;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Owner {

    public static void ownerDashboard() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        boolean loop = true;

        while(loop) {
            System.out.println("\n==============================================");
            System.out.println("         OWNER DASHBOARD - CROCHET SHOP");
            System.out.println("==============================================");
            System.out.println(" [1] View Sales Report");
            System.out.println(" [2] Add Manager");
            System.out.println(" [3] Logout");
            System.out.println("==============================================");
            System.out.print(" Choose (1 - 3): ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch(choice) {
                case 1: viewSalesReport(db); break;
                case 2: addManager(sc, db);break;
                case 3: 
                    System.out.println("\nLogging out...");
                    loop = false;
                break;
                default: System.out.println("\nInvalid choice. Try again.");
            }
        }
    }

    private static void viewSalesReport(config db) {
        
        System.out.println("\n=== SALES REPORT ===");

        List<Map<String, Object>> sales = db.fetchRecords(
            "SELECT o.order_id, p.product_name, o.quantity, o.total_price, o.order_date " +
            "FROM OrderTable o " +
            "JOIN Product p ON o.product_id = p.product_id"
        );

        if (sales.isEmpty()) {
            System.out.println("No sales yet.");
        } else {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-8s | %-10s | %-15s |\n", "Order ID", "Product", "Quantity", "Total", "Date");
            System.out.println("--------------------------------------------------------------------------------");

            double totalSales = 0; 

            for (Map<String, Object> s : sales) {
                double price = s.get("total_price") instanceof Double ? (Double) s.get("total_price") : Double.parseDouble(s.get("total_price").toString());
                totalSales += price;

                System.out.printf("| %-10s | %-20s | %-8s | ₱%-9.2f | %-15s |\n",
                    s.get("order_id"), s.get("product_name"), s.get("quantity"), price, s.get("order_date"));
            }
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("| TOTAL SALES : ₱%-9.2f  \n", totalSales);
            System.out.println("--------------------------------------------------------------------------------");
        }
}


    private static void addManager(Scanner sc, config db) {
        System.out.println("\n=== ADD MANAGER ===");
        System.out.print("Enter Manager Full name: ");
        String name = sc.nextLine();
        System.out.print("Enter Manager Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        
        String hashedPassword = config.hashPassword(password);
        
        String sql = "INSERT INTO User (email, password, role, full_name) VALUES (?, ?, ?, ?)";
        
        db.addRecord(sql, email, hashedPassword, "Manager", name);

        System.out.println("\nManager added successfully!");
    }
}
