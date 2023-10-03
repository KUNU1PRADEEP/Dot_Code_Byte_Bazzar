
package Dot_Code_Byte_Bazzar;
import java.sql.*;
import java.util.Scanner;

import java.util.Scanner;

import static java.lang.Class.forName;

public class Byte_Bazzar {
    private static final String url = "jdbc:mysql://localhost:3306/dotcode";
    private static final String username = "root";
    private static final String password = "Kunal@1234";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner =  new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long batch_number;

            while(true){
                System.out.println("*** DOT CODE BYTE BAZZAR ***");
                System.out.println();
                System.out.println("1. Register Yourself");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Get membership of Dot Code Society");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1) {
                                    batch_number = accounts.enroll_account(email);
                                    System.out.println("Congratulation, for taking 1 step toward exploring.\n You are successfully assigned as Dot Code Society Member.");
                                    System.out.println("Your Dot Code Batch Number is: " + batch_number);
                                }else{
                                    break;
                                }

                            }
                            batch_number = accounts.getAccount_number(email);


                            System.out.println("Dot Code Society     ---->  GOODIES");
                            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
                            System.out.println("|             CATEGORY             |              ITEM                    |      COINS TO ACHIEVE   |");
                            System.out.println("|  1           Tech Gadgets        |              Headphones              |      900                |");
                            System.out.println("|                                  |              Electric Tooth-brush    |      350                |");
                            System.out.println("|                  .....           |                                      |                         |");
                            System.out.println("|  2      Learning Resources       |            Skill-based Courses       |      300                |");
                            System.out.println("|                                  |            Software Licenses         |      500                |");
                            System.out.println("|                .......           |                                      |                         |");
                            System.out.println("|  3  Practical Coding Accessorie  |             Keyboards/Mice           |      700                |");
                            System.out.println("|                                  |                   USB Drives         |      750                |");
                            System.out.println("|                 ....             |                                      |                         |");
                            System.out.println("|  4      Coding Society Swag      |           T-shirts/Hoodies           |      600                |");
                            System.out.println("|                                  |            Customized Coding Swag    |      550                |");
                            System.out.println("|                   ......         |                                      |                         |");
                            System.out.println("|  5            Others             |             Motivational Quote Frame |      350                |");
                            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");



                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Exchange Coins with.");
                                System.out.println("2. Credit to Dot Code.{only for Dot Code Society}");
                                System.out.println("3. Award Coin.{only for Dot Code Society}");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.Redeem_Coins(batch_number);
                                        break;
                                    case 2:
                                        accountManager.credit_coins(batch_number);
                                        break;
                                    case 3:
                                        accountManager.Distribute_Coin(batch_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(batch_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING Dot Code Byte Bazzar!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
