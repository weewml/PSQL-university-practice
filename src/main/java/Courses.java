import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Courses {
    /* 
    вывод всего сделан
    поиск по названию сделан
    изменение названия сделано
    добавление курса сделано
    удаление по айди курса и айди профессора сделано
    */

    // все курсы
    protected static void getAllCourses(Connection connection) throws SQLException {

        String column0 = "id", column1 = "title", column2 = "id_professor";

        int param0;
        String param1;
        int param2;

        Statement statement = connection.createStatement();     // создаем оператор для простого запроса (без параметров)
        ResultSet rs = statement.executeQuery("SELECT * FROM courses;"); // выполняем запроса на поиск и получаем список ответов

        int count = rs.getMetaData().getColumnCount();  
        // чтобы вывести названия столбцов
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
            param2 = rs.getInt(column2);
            param1 = rs.getString(column1);
            param0 = rs.getInt(column0);
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }
        statement.close();
        System.out.println();
    }

    // поиск курсов по названию
    protected static void getCoursesByTitle(Connection connection, String title) throws SQLException {
        if (connection == null || connection.isClosed()) return; 
        if (title == null || title.isBlank()) return; 

        title = '%' + title + '%'; // слово может быть дополнено слева или справа

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM courses " +
                        "WHERE title LIKE ?;");          // создаем оператор шаблонного-запроса с параметрами - ?
        statement.setString(1, title);   
        ResultSet rs = statement.executeQuery();        // выполняем запроса на поиск и получаем список ответов

        int count = rs.getMetaData().getColumnCount();  
        for (int i = 1; i <= count; i++){ 
            if (count == i) {System.out.print(rs.getMetaData().getColumnLabel(i));}
            else {System.out.print(rs.getMetaData().getColumnLabel(i) + " | ");}
        }
        System.out.println();

        while (rs.next()) {  
            String param = "";
            for (int i = 1; i <= count; i++) {
                param += rs.getString(i);
                if (i != count) param += " | ";
            }
            System.out.println(param);
        }
        statement.close();
        System.out.println();
    }

    // добавление курса
    protected static void addCourse(Connection connection, String title, int id_professor) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (title == null || title.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO courses(title, id_professor) VALUES (?, ?);"
        );
        statement.setString(1, title);
        statement.setInt(2, id_professor);

        int count = statement.executeUpdate();
        System.out.println("INSERTed " + count + " courses");

        statement.close();
        getAllCourses(connection);
    }

    // корректировка курсы по его айди
    protected static void correctCourseById(Connection connection, int id, String title) throws SQLException {
        if (connection == null || connection.isClosed()) return;
        if (title == null || title.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE courses SET title = ? WHERE id = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, title);
        statement.setInt(2, id);

        int count = statement.executeUpdate();
        System.out.println("UPDATed " + count + " courses");
        ResultSet rs = statement.getGeneratedKeys();
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3));
        }
        statement.close();
    }

    // удаление курса по его айди
    protected static void deleteCourseById(Connection connection, int id) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM courses WHERE id = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );

        statement.setInt(1, id);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        System.out.println("DELETEd " + count + " courses");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3));
        }

        statement.close();
    }

    // удаление всех курсов от определенного профессора (по айди профессора)
    protected static void deleteCoursesByIdProfessor(Connection connection, int id_professor) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM courses WHERE id_professor = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );

        statement.setInt(1, id_professor);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        System.out.println("DELETEd " + count + " courses");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) +
                    " | " + rs.getString(3));
        }

        statement.close();
    }
}
