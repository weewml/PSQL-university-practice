import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Professors {

    /*
    поиск по имени сделан
    изменение кафедры псделано
    добавление профессора сделано
    удаление по айди сделано
    */


    // вывод всех профессоров
    protected static void getAllProfessors(Connection connection) throws SQLException {
        
        String column0 = "id", column1 = "full_name", column2 = "department";
        
        int param0;
        String param1, param2;

        Statement statement = connection.createStatement();     // создаем оператор для простого запроса (без параметров)
        ResultSet rs = statement.executeQuery("SELECT * FROM professors;"); // выполняем запроса на поиск и получаем список ответов

        int count = rs.getMetaData().getColumnCount();  
        
        for (int i = 1; i <= count; i++){

            if (count == i) {
                System.out.print(rs.getMetaData().getColumnLabel(i));
            }
            else {
                System.out.print(rs.getMetaData().getColumnLabel(i) + " | ");
            }
        }
        System.out.println(); 

        while (rs.next()) { 
            param2 = rs.getString(column2);
            param1 = rs.getString(column1);
            param0 = rs.getInt(column0);
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    protected static void getProfessorsByName(Connection connection, String name) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (name == null || name.isBlank()) return; 

        String param;
        name = '%' + name + '%'; // переданное значение может быть дополнено (%) сначала и/или в конце (часть слова)

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM professors " +
                        "WHERE full_name LIKE ?;");              // создаем оператор шаблонного-запроса с "включаемыми" параметрами - ?
        statement.setString(1, name);     // "безопасное" добавление параметров в запрос; с учетом их типа и порядка (индексация с 1)
        ResultSet rs = statement.executeQuery();        // выполняем запроса на поиск и получаем список ответов

        int count = rs.getMetaData().getColumnCount();  
        
        for (int i = 1; i <= count; i++){

            if (count == i) {
                System.out.print(rs.getMetaData().getColumnLabel(i));
            }
            else {
                System.out.print(rs.getMetaData().getColumnLabel(i) + " | ");
            }
        }
        System.out.println(); 

        while (rs.next()) {  
            param = "";
            for (int i = 1; i <= count; i++) {
                param += rs.getString(i);
                if (i != count) param += " | ";
            }
            System.out.println(param);
        }

        statement.close();
        System.out.println();
    }

    protected static void addProfessor(Connection connection, String full_name, String department) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (full_name == null || full_name.isBlank()) return;
        if (department == null || department.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO professors(full_name, department) VALUES (?, ?);"
        );
        statement.setString(1, full_name);
        statement.setString(2, department);

        int count = statement.executeUpdate();
        System.out.println("INSERTed " + count + " professors");

        statement.close();
        getAllProfessors(connection);
    }

    protected static void correctProfessor(Connection connection, int id, String department) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        if (department == null || department.isBlank()) return; 

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE professors SET department = ? WHERE id = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, department);
        statement.setInt(2, id);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("UPDATed " + count + " professors");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3));
        }

        statement.close();
    }

    protected static void deleteProfessorById(Connection connection, int id) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM professors WHERE id = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );

        statement.setInt(1, id);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        System.out.println("DELETEd " + count + " professors");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3));
        }

        statement.close();
    }
}
