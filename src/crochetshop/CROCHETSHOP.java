
package crochetshop;

import config.config;
import static config.config.hashPassword;
import static crochetshop.Manager.managerDashboard;
import static crochetshop.Owner.ownerDashboard;
import static crochetshop.Provider.providerDashboard;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class CROCHETSHOP {

    
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        
    while(run){
        
        System.out.println("=============================================");
        System.out.println("       Crochet Shop Management System");
        System.out.println("=============================================\n");
        System.out.println(" [1] Login");
        System.out.println(" [2] Register");
        System.out.println(" [3] Exit");
        System.out.println("==============================================");
        System.out.print(" Choose (1 - 3): ");
        
        String res = sc.nextLine();
        
            switch (res){
                
                case "1":
                    
                    login();
                    break;
                    
                case "2":
                    
                    register();
                    break;
                    
                case "3":
                    
                    System.out.println("\nThank you for using Crochet Shop Management System!");
                    run = false;
                    
                    break;
                    
                default:
                    
                    System.out.println("\nInvalid option! Please choose 1-3.");
                
            }

        
    }
        
    }
    
    private static void login() {

    Scanner sc = new Scanner(System.in);
    config db = new config();

    System.out.print("\nEnter Email: ");
    String email = sc.nextLine();

    System.out.print("Enter password: ");
    String password = sc.nextLine();

    String hash = hashPassword(password);

    String sql = "SELECT user_id, role FROM User WHERE Email = ? AND password = ?";
    List<Map<String, Object>> log = db.fetchRecords(sql, email, hash);

    if (log.isEmpty()) {
        System.out.println("\nINVALID CREDENTIALS");
        return;
    }

    String role = log.get(0).get("role").toString();

    switch(role){
        case "Owner": 
            
            ownerDashboard(); 
            
            break;
        case "Manager": 
            
            managerDashboard(); 
            
            break;
        case "Provider":
            
            providerDashboard();
            
            break;
            
        default:
            System.out.println("\nUnknown role, contact the administrator.");
    }
}

    
    private static void register(){

        Scanner sc = new Scanner(System.in);
        config db = new config();
        String roletype = null;

        System.out.println("\n=== REGISTER NEW USER ===");
        System.out.print("Enter full name: ");
        String fullName = sc.nextLine();

        System.out.print("Enter Email: ");
        String newemail = sc.nextLine();


        while (true) {
            String checkQry = "SELECT * FROM User WHERE Email = ?";
            List<Map<String, Object>> existing = db.fetchRecords(checkQry, newemail);

            if (existing.isEmpty()) break;

            System.out.print("\nEmail already exists. Enter a different one: ");
            newemail = sc.nextLine();
        }


        int roleChoice;
        do {
            System.out.println("\nSelect user role:");
            System.out.println(" [1] Owner");
            System.out.println(" [2] Manager");
            System.out.println(" [3] Provider");
            System.out.print("Choose (1 - 3): ");

            roleChoice = sc.nextInt();
            sc.nextLine(); 

        } while (roleChoice < 1 || roleChoice > 3);

        switch (roleChoice) {
            case 1: roletype = "Owner"; break;
            case 2: roletype = "Manager"; break;
            case 3: roletype = "Provider"; break;
        }


        System.out.print("Enter password: ");
        String newPass = sc.nextLine();
        String hashPass = config.hashPassword(newPass);


        String sql = "INSERT INTO User (Email, password, full_name, role) VALUES (?, ?, ?, ?)";
        db.addRecord(sql, newemail, hashPass, fullName, roletype);

        System.out.println("\nRegistration successful! You can now log in.");
    }

   
    
}
