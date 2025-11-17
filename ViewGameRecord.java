import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ViewGameRecord {
    public static void main(String[] args) {

        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ccinfom",
                    "root","Password1234"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM game_record");

            System.out.println("| Game ID | title | genre | release date | platform | price " +
                    "| total bought | status | review average | total reviews | publisher ID |");

            while(resultSet.next()){
                System.out.print("| " + resultSet.getInt("game_id") + " | ");
                System.out.print(resultSet.getString("title") + " | ");
                System.out.print(resultSet.getString("genre") + " | ");
                System.out.print(resultSet.getDate("release_date") + " | ");
                System.out.print(resultSet.getString("platform") + " | ");
                System.out.print(resultSet.getFloat("price") + " | ");
                System.out.print(resultSet.getInt("total_bought") + " | ");
                System.out.print(resultSet.getString("status") + " | ");
                System.out.print(resultSet.getFloat("review_average") + " | ");
                System.out.print(resultSet.getInt("reviews") + " | ");
                System.out.println(resultSet.getInt("publisher_id") + " |");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
