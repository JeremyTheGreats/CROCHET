
package crochetshop;

import config.config;
import static config.config.hashPassword;
import static crochetshop.Manager.Manager;
import static crochetshop.Owner.Owner;
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
        
        int res = sc.nextInt();
        
            switch (res){
                
                case 1:
                    
                    login();
                    break;
                    
                case 2:
                    
                    register();
                    break;
                    
                case 3:
                    
                    System.out.println("\nThank you for using Crochet Shop Management System!");
                    run = false;
                    
                    break;
                    
                default:
                    
                    System.out.println("\nInvalid option! Please choose 1-3.");
                
            }

        
    }
        
    }
    
    private static void login(){
        
        Scanner sc = new Scanner(System.in);
        config db = new config();
        
        System.out.print("\nEnter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();
        
        String hash = hashPassword(password);
        
        String sql = "SELECT * FROM User WHERE email = ? and password = ?";
        
        List<Map<String, Object>> log = db.fetchRecords(sql, email, hash);
        
        if (log.isEmpty()){
            
            System.out.println("\nINVALID CREDENTIALS");
        }
        else{
            
            String role = log.get(0).get("role").toString();
            
                switch(role){
                    case "Owner":
                        
                        Owner();
                        break;
                        
                    case "Manager":
                        
                        Manager();
                        break;
                        
                    case "Provider":
                        
                        System.out.println("\nNot Available for now. Try another ROLE.");
                        break;
                        
                }
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
            String checkQry = "SELECT * FROM User WHERE email = ?";
            
            List<Map<String, Object>> existing = db.fetchRecords(checkQry, newemail);
            
                if (existing.isEmpty()) {
                    break;
                }
                
            System.out.print("\nUsername already exists. Enter a different one: ");
            newemail = sc.nextLine();
            
        }
        
        System.out.println("\nSelect user role:");
        System.out.println(" [1] Owner");
        System.out.println(" [2] Manager");
        System.out.println(" [3] Provider");
        System.out.print("Choose (1 - 3): ");
        
        int roleChoice = sc.nextInt();
        sc.nextLine();
    
    do{
        
            if (roleChoice == 1){
                
                roletype = "Owner";
            }
            else if (roleChoice == 2){
                
                roletype = "Manager";
            }
            else if (roleChoice == 3){
                
                roletype = "Provider";
            }
            else{
                
                System.out.print("Invalid, choose 1–3 only: ");
            }
        
    }while(roleChoice < 1 && roleChoice >3);
        
    
        System.out.print("Enter password: ");
        String newPass = sc.nextLine();
        String hashPass = config.hashPassword(newPass);
        
        String sql = "INSERT INTO User (Email, password, full_name, role) VALUES (?, ?, ?, ?)";
        db.addRecord(sql, newemail, hashPass, fullName, roletype);

        System.out.println("\nRegistration successful! You can now log in.");
        
    }
    
}
