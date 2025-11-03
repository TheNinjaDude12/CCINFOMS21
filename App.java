import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        buyGame();
    }
    
    public static void getDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Id: ");
        int number = sc.nextInt();

        try {
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                "root", "thunder1515");

            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM customer_record WHERE customer_id = ?");
            pstmt.setInt(1, number);
            ResultSet resultSet = pstmt.executeQuery();
           
            if (resultSet.next()) {
                System.out.println("\n=== Customer Details ===");
                System.out.println("Customer ID: " + resultSet.getInt("customer_id"));
                System.out.println("First Name: " + resultSet.getString("first_name"));
                System.out.println("Last Name: " + resultSet.getString("last_name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Registration Date: " + resultSet.getDate("registration_date"));
                System.out.println("Country: " + resultSet.getString("country"));
                System.out.println("Preferred Platform: " + resultSet.getString("preferred_platform"));
                System.out.println("Total Spent: $" + resultSet.getFloat("total_spent"));
                System.out.println("Games Owned: " + resultSet.getInt("games_owned"));
            } else {
                System.out.println("Customer not found!");
            }
            
            connection.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void buyGame() {
        Scanner sc = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                "root", "thunder1515");

            System.out.print("Enter customer Id: ");
            int customer_id = sc.nextInt();
          
            PreparedStatement checkCustomer = connection.prepareStatement(
                "SELECT customer_id FROM customer_record WHERE customer_id = ?");
            checkCustomer.setInt(1, customer_id);
            ResultSet resultSet = checkCustomer.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Customer does not exist!");
                connection.close();
                return;
            }
            
            System.out.print("Enter game Id: ");
            int game_id = sc.nextInt();
            sc.nextLine(); // Consume newline
            
           
            PreparedStatement checkGame = connection.prepareStatement(
                "SELECT price, status FROM game_record WHERE game_id = ?");
            checkGame.setInt(1, game_id);
            ResultSet resultSet2 = checkGame.executeQuery();
            
            if (!resultSet2.next()) {
                System.out.println("Game does not exist!");
                connection.close();
                return;
            }
            
            float gamePrice = resultSet2.getFloat("price");
            String status = resultSet2.getString("status");
            
            if (status.equals("under_dev")) {
                System.out.println("Game is under development and cannot be purchased!");
                connection.close();
                return;
            }
          PreparedStatement checkOwnership = connection.prepareStatement(
                "SELECT transaction_id FROM transaction_log WHERE customer_id = ? AND game_id = ?");
            checkOwnership.setInt(1, customer_id);
            checkOwnership.setInt(2, game_id);
            ResultSet resultSetCustomerCheck = checkOwnership.executeQuery();

            if (resultSetCustomerCheck.next()) {
                System.out.println("Game already owned!");
                connection.close();
                return;
            }
            
            System.out.print("Enter Payment Method: ");
            String payment = sc.nextLine();
        
           
            PreparedStatement insertTransaction = connection.prepareStatement(
                "INSERT INTO transaction_log(customer_id, game_id, payment_method, amount) VALUES(?, ?, ?, ?)");
            insertTransaction.setInt(1, customer_id);
            insertTransaction.setInt(2, game_id);
            insertTransaction.setString(3, payment);
            insertTransaction.setFloat(4, gamePrice);
            insertTransaction.executeUpdate();

            PreparedStatement updateCustomerOwned = connection.prepareStatement(
                "UPDATE customer_record SET games_owned = games_owned + 1 WHERE customer_id = ?");
            updateCustomerOwned.setInt(1, customer_id);
            updateCustomerOwned.executeUpdate();

        
            PreparedStatement updateCustomerSpent = connection.prepareStatement(
                "UPDATE customer_record SET total_spent = total_spent + ? WHERE customer_id = ?");
            updateCustomerSpent.setFloat(1, gamePrice);
            updateCustomerSpent.setInt(2, customer_id);
            updateCustomerSpent.executeUpdate();

         
            PreparedStatement updateGameTotalBought = connection.prepareStatement(
                "UPDATE game_record SET total_bought = total_bought + 1 WHERE game_id = ?");
            updateGameTotalBought.setInt(1, game_id);
            updateGameTotalBought.executeUpdate();
            
            System.out.println("\nsGame purchased successfully!");
            System.out.println("Amount paid: $" + gamePrice);
            
            connection.close();
                    
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewOwnedGames() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Id: ");
        int customer_id = sc.nextInt();

        try {
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/gamemanagementdatabase",
                "root", "thunder1515");

            //  get customer details
            PreparedStatement getCustomer = connection.prepareStatement(
                "SELECT * FROM customer_record WHERE customer_id = ?");
            getCustomer.setInt(1, customer_id);
            ResultSet rsCustomer = getCustomer.executeQuery();
            
            if (!rsCustomer.next()) {
                System.out.println("Customer does not exist!");
                connection.close();
                return;
            }
            
            // Display customer info
            System.out.println("\n=== Customer Details ===");
            System.out.println("Customer ID: " + rsCustomer.getInt("customer_id"));
            System.out.println("First Name: " + rsCustomer.getString("first_name"));
            System.out.println("Last Name: " + rsCustomer.getString("last_name"));
            System.out.println("Email: " + rsCustomer.getString("email"));
            System.out.println("Registration Date: " + rsCustomer.getDate("registration_date"));
            System.out.println("Country: " + rsCustomer.getString("country"));
            System.out.println("Preferred Platform: " + rsCustomer.getString("preferred_platform"));
            System.out.println("Total Spent: $" + rsCustomer.getFloat("total_spent"));
            System.out.println("Games Owned: " + rsCustomer.getInt("games_owned"));
            
            // Get games purchased
            System.out.println("\n=== Games Purchased ===");
            PreparedStatement getGames = connection.prepareStatement(
                "SELECT gr.title, gr.price, tl.purchase_date " +
                "FROM game_record gr " +
                "JOIN transaction_log tl ON tl.game_id = gr.game_id " +
                "WHERE tl.customer_id = ? " +
                "ORDER BY tl.purchase_date DESC");
            getGames.setInt(1, customer_id);
            ResultSet rsGames = getGames.executeQuery();
            
            int count = 0;
            while (rsGames.next()) {
                count++;
                System.out.println(count + ". " + rsGames.getString("title") + 
                                 " - $" + rsGames.getFloat("price") + 
                                 " (Purchased: " + rsGames.getDate("purchase_date") + ")");
            }
            
            if (count == 0) {
                System.out.println("No games purchased yet.");
            }
            
            connection.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}