import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Students {

    /*  вывод всего сделан
    поиск по имени сделан
    добавление студента сделано
    изменение группы и уровня образования сделано
    удаление по айди сделано
    */

    protected static void getAllStudents(Connection connection) throws SQLException {

        int param0 = -1;
        String param1 = null, param2 = null, param3 = null;

        Statement statement = connection.createStatement();                 // создаем оператор для простого запроса (без параметров)
        ResultSet rs = statement.executeQuery("SELECT * FROM students;");  // выполняем запроса на поиск и получаем список ответов

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
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getString(3);
            param3 = rs.getString(4);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }

        statement.close();
        System.out.println();
    }

    protected static void getStudentsByName(Connection connection, String name) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (name == null || name.isBlank()) return;

        String param;
        name = '%' + name + '%'; // переданное значение может быть дополнено

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM students " +
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

    protected static void addStudent(Connection connection, String full_name, String group_name, String education_level) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (full_name == null || full_name.isBlank()) return;
        if (group_name == null || group_name.isBlank()) return;
        if (education_level == null || education_level.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO students(full_name, group_name, education_level) VALUES (?, ?, ?);");
        statement.setString(1, full_name);
        statement.setString(2, group_name);
        statement.setString(3, education_level);

        int count = statement.executeUpdate();
        System.out.println("INSERTed " + count + " students");
        statement.close();
    }

    protected static void correctStudentById(Connection connection, int id, String group_name, String education_level) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        if (group_name == null || group_name.isBlank()) return;
        if (education_level == null || education_level.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                    "UPDATE students " +
                        "SET group_name = ?, education_level = ? " +
                        "WHERE id = ? " +
                        "RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, group_name);
        statement.setString(2, education_level);
        statement.setInt(3, id);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("UPDATed " + count + " students");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3) + " | " + rs.getString(4));
        }

        statement.close();
    }

    protected static void deleteStudentById(Connection connection, int id) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM students WHERE id = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );

        statement.setInt(1, id);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        System.out.println("DELETEd " + count + " students");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3) + " | " + rs.getString(4));
        }

        statement.close();
    }
}