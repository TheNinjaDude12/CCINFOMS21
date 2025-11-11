import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TextBasedData {
    public static void main(String[] args) throws Exception {
        getDetails();
    }

    public static void getDetails () throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Publisher number: ");
        int pubNum = sc.nextInt();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/CCINFOMDataBase",
                    "root",
                    "");

            PreparedStatement prepStat = conn.prepareStatement(
                    "SELECT * FROM publisher_record WHERE publisher_id = ?");
            prepStat.setInt(1, pubNum);
            ResultSet resultSet = prepStat.executeQuery();

            if(resultSet.next()) {
                System.out.println("=-=-=-= Publisher Records =-=-=-=");
                System.out.println("Publisher ID:     " + resultSet.getInt("publisher_id"));
                System.out.println("Name:             " + resultSet.getString("name"));
                System.out.println("Country:          " + resultSet.getString("country"));
                System.out.println("Established Date: " + resultSet.getDate("established_date"));
                System.out.println("Website:          " + resultSet.getString("website"));
                System.out.println("Email:            " + resultSet.getString("contact_email"));
                System.out.println("Specialization:   " + resultSet.getString("specialization"));
                System.out.println("Total Games:      " + resultSet.getInt("total_games_published"));
            } else {
                System.out.println("Publisher does not exist");
            }
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void getGamesPub() {

    }

    public static void addPubGame() {

    }
}