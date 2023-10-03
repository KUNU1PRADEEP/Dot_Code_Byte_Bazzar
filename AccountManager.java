package Dot_Code_Byte_Bazzar;


import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }


    public void credit_coins(long batch_number)throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Initial  Coin: ");
        int coin = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(batch_number == 10000101) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE batch_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, batch_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET Coins_Collected = Coins_Collected + ? WHERE batch_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setInt(1, coin);
                    preparedStatement1.setLong(2, batch_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Coin :"+coin+" credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void Redeem_Coins(long batch_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Coin: ");
        int coin = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(batch_number!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE batch_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, batch_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int Coins_Collected = resultSet.getInt("Coins_Collected");
                    if (coin<=Coins_Collected){
                        String redeem_query = "UPDATE Accounts SET Coins_Collected = Coins_Collected - ? WHERE batch_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(redeem_query);
                        preparedStatement1.setInt(1,coin );
                        preparedStatement1.setLong(2, batch_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Coin "+coin+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Coins, oops!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
                e.printStackTrace();
            }
        connection.setAutoCommit(true);
    }

    public void Distribute_Coin(long sender_batch_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Dot Code Batch Number: ");
        long receiver_batch_number = scanner.nextLong();
        System.out.print("Enter Coin: ");
        int coin = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_batch_number==10000101 && receiver_batch_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE batch_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_batch_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int current_Coins_Collected = resultSet.getInt("Coins_Collected");
                    if (coin<=current_Coins_Collected){

                        // Write debit and credit queries
                        String redeem_query = "UPDATE Accounts SET Coins_Collected = Coins_Collected - ? WHERE batch_number = ?";
                        String credit_query = "UPDATE Accounts SET Coins_Collected = Coins_Collected + ? WHERE batch_number = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(redeem_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1, coin);
                        creditPreparedStatement.setLong(2, receiver_batch_number);
                        debitPreparedStatement.setInt(1, coin);
                        debitPreparedStatement.setLong(2, sender_batch_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("No of  "+coin+" Coins  Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid Dot Code Batch  number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long batch_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Coins_Collected FROM Accounts WHERE batch_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, batch_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int Coins_Collected = resultSet.getInt("Coins_Collected");
                System.out.println("Coins_Collected: "+Coins_Collected);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}

