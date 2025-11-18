import java.sql.*;
import java.util.Scanner;

public class ViewGameRecord {

    public static void displayGameDetails() {
        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ccinfom",
                    "root","Password1234");

            Scanner sc = new Scanner(System.in);

            System.out.println("[ Display Game Details ]");
            System.out.print("Enter Game ID: ");
            int gameID = sc.nextInt();
            String buffer = sc.nextLine();

            PreparedStatement gameRecord = connection.prepareStatement("SELECT * FROM game_record WHERE Game_id = ?");
            gameRecord.setInt(1, gameID);
            ResultSet rsGameRecord = gameRecord.executeQuery();

            if(!rsGameRecord.next()){
                System.out.println("Game Does Not Exist");
            }
            else{
                System.out.println("\nGame ID: " + rsGameRecord.getInt("Game_id"));
                System.out.println("Title: " + rsGameRecord.getString("title"));
                System.out.println("Genre: " + rsGameRecord.getString("genre"));
                System.out.println("Release Date: " + rsGameRecord.getDate("release_date"));
                System.out.println("Platform: " + rsGameRecord.getString("platform"));
                System.out.println("Price: $" + rsGameRecord.getString("price"));
                System.out.println("Total Bought: " + rsGameRecord.getInt("total_bought"));
                System.out.println("Status: " + rsGameRecord.getString("status"));
                System.out.println("Ratings: " + rsGameRecord.getFloat("review_average"));
                System.out.println("Total Reviews: " + rsGameRecord.getInt("reviews"));
                System.out.println("Publisher ID: " + rsGameRecord.getInt("publisher_id") + "\n");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void displayGameCustomers() {
        try{
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ccinfom",
                    "root","Password1234");

            Scanner sc = new Scanner(System.in);

            System.out.println("[ Display Game Customers ]");
            System.out.print("Enter Game ID: ");
            int gameID = sc.nextInt();
            String buffer = sc.nextLine();

            PreparedStatement gameRecord = connection.prepareStatement("SELECT * FROM game_record WHERE Game_id = ?");
            gameRecord.setInt(1, gameID);
            ResultSet rsGameRecord = gameRecord.executeQuery();

            if(!rsGameRecord.next()){
                System.out.println("Game Does Not Exist");
            }
            else if(!rsGameRecord.getString("status").equals("Released")){
                System.out.println("Game Has Not Been Released");
            }
            else{
                PreparedStatement gameCustomers = connection.prepareStatement("SELECT * FROM transaction_log t " +
                        "JOIN customer_record c ON t.customer_id = c.customer_id " +
                        "WHERE game_id = ? AND status = ?");
                gameCustomers.setInt(1, gameID);
                gameCustomers.setString(2, "Paid");
                ResultSet rsGameCustomers = gameCustomers.executeQuery();

                System.out.println("Customers:");

                while(rsGameCustomers.next()){
                    String firstName = rsGameCustomers.getString("first_name");
                    String lastName = rsGameCustomers.getString("last_name");
                    System.out.println(firstName + " " + lastName);
                }

                rsGameCustomers.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        displayGameDetails();
        displayGameCustomers();

        System.exit(0);
    }
}
