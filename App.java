
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        getDetails();
        
    }
    public static void getDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter customer Id: ");
        int number = sc.nextInt();
        
        

        try {
            Connection connection = DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/gamingdatabase",
            "root", "thunder1515");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
            "SELECT * FROM customer_record WHERE customer_id=" + number);
        while(resultSet.next()) {
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
}
