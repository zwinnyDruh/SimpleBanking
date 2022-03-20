import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Query {
    SQLiteDataSource dataSource;

    Query(String path) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(path);
    }

    ArrayList processQuery(String query) {
        ArrayList<String[]> output = new ArrayList<>();

        // setting a connection
        try (Connection con = dataSource.getConnection()) {

            // creating statement to process queries
            try (Statement statement = con.createStatement()) {
                ResultSet result = statement.executeQuery(query);

                // prepare array to return
                while (result.next()) {
                    String[] recordsArray = new String[4];
                    //todo replace with loop
                    recordsArray[0] = result.getString(1);
                    recordsArray[1] = result.getString(2);
                    recordsArray[2] = result.getString(3);
                    recordsArray[3] = result.getString(4);
                    output.add(recordsArray);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output;
    }

    int processUpdate(String query) {

        // setting a connection
        try (Connection con = dataSource.getConnection()) {

            // creating statement to process queries
            try (Statement statement = con.createStatement()) {
                return statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return -1;
    }


}
