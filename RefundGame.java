import java.sql.*;
import java.util.Scanner;

public class RefundGame {

    public static void refundGame (){
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ccinfom",
                    "root","Password1234");

            int customerID;
            int gameID;
            String buffer;

            Scanner sc = new Scanner(System.in);

            System.out.println("[ REFUND GAME ]");
            System.out.print("Enter Customer ID: ");
            customerID = sc.nextInt();
            buffer = sc.nextLine();

            PreparedStatement checkAccount = connection.prepareStatement("SELECT * FROM customer_record WHERE customer_id = ?");
            checkAccount.setInt(1,customerID);
            ResultSet rsCustomer = checkAccount.executeQuery();

            if(!rsCustomer.next()){
                System.out.println("Customer Does Not Exist");
            }
            else {
                System.out.print("Enter Game ID: ");
                gameID = sc.nextInt();
                buffer = sc.nextLine();

                PreparedStatement checkTransaction = connection.prepareStatement("SELECT * FROM transaction_log WHERE customer_id = ? AND game_id = ?");
                checkTransaction.setInt(1, customerID);
                checkTransaction.setInt(2, gameID);
                ResultSet rsTransaction = checkTransaction.executeQuery();

                if(!rsTransaction.next()){
                    System.out.println("Customer Does Not Own the Game.");
                    rsTransaction.close();
                }
                else {
                    if(rsTransaction.getString("status").equals("Paid")){
                        PreparedStatement updateTransaction = connection.prepareStatement("UPDATE transaction_log SET status = 'Refunded' WHERE customer_id = ? AND game_id = ?");
                        updateTransaction.setInt(1, customerID);
                        updateTransaction.setInt(2, gameID);
                        int rowTransaction = updateTransaction.executeUpdate();

                        float refund = rsTransaction.getFloat("amount");
                        PreparedStatement updateCustomer =  connection.prepareStatement("UPDATE customer_record SET total_spent = total_spent - ?, " +
                                                                                            "games_owned = games_owned - ? WHERE customer_id = ?");
                        updateCustomer.setFloat(1, refund);
                        updateCustomer.setInt(2, 1);
                        updateCustomer.setInt(3, customerID);
                        int rowCustomer = updateCustomer.executeUpdate();

                        PreparedStatement updateGameRecord = connection.prepareStatement("UPDATE game_record SET total_bought = total_bought - ? WHERE game_id = ?");
                        updateGameRecord.setInt(1, 1);
                        updateGameRecord.setInt(2, gameID);
                        int rowGame = updateGameRecord.executeUpdate();

                        System.out.println("Game Successfully Refunded!");
                        rsTransaction.close();
                    }
                    else {
                        System.out.println("Game Cannot Be Refunded.");
                        rsTransaction.close();
                    }
                }
            }

            rsCustomer.close();
            sc.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        refundGame ();

        System.exit(0);
    }
}
