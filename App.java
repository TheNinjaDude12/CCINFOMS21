
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        getDetails();
        buyGame();
    }
    public static void getDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Id: ");
        int number = sc.nextInt();
        
        

        try {
            System.out.println("Works");
            Connection connection = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/gamingdatabase",
            "root", "thunder1515");

        Statement statement = connection.createStatement();
        
        ResultSet resultSet = statement.executeQuery(
            "SELECT * FROM customer_record WHERE customer_id=" + number);
       
        while(resultSet.next()) {
            System.out.println("Data");
            System.out.println("First Name: " + resultSet.getString("first_name"));
            System.out.println("Last Name: " + resultSet.getString("last_name"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Registration Date: " + resultSet.getString("registration_date"));
            System.out.println("Country: " + resultSet.getString("country"));
            System.out.println("Preferred Platform: " + resultSet.getString("preferred_platform"));
            System.out.println("Total Spent: " + resultSet.getFloat("total_spent"));
            System.out.println("Games Owned: " + resultSet.getInt("games_owned"));

        }
        }catch(SQLException e) {
            e.printStackTrace();
        }


    }
    public static void buyGame() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Id: ");
        int number = sc.nextInt();
        System.out.print("Enter Game Id: ");
        int number2 = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Payment Method: ");
        String payment = sc.nextLine();
         try {
            Connection connection = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/gamingdatabase",
            "root", "thunder1515");

        Statement statement = connection.createStatement();
        Statement statement2 = connection.createStatement();
        
        ResultSet resultSet = statement.executeQuery(
            "SELECT * FROM customer_record WHERE customer_id=" + number);
        ResultSet resultSet2 = statement2.executeQuery(
            "SELECT * FROM game_record WHERE game_id=" + number2
        );

         if(!resultSet.next()) {
           System.out.println("Customer does not exist!");
           return;
        }
        if(!resultSet2.next()) {
            System.out.println("Game does not exist!");
            return;
        }
        float gamePrice = resultSet2.getInt("price");
       
       PreparedStatement pstmt = connection.prepareStatement(
    "INSERT INTO transaction_log(customer_id, game_id, payment_method, amount) VALUES(?, ?, ?, ?)");
            pstmt.setInt(1, number);
            pstmt.setInt(2, number2);
            pstmt.setString(3, payment);
            pstmt.setFloat(4, gamePrice);

pstmt.executeUpdate();
                

       
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}


