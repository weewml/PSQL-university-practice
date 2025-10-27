import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Enrollments {
    /* 
    вывод всего сделан
    поиск по записям студента сделан
    изменение оценки за курс сделано
    добавление записи сделано (с датой и без)
    удаление определенной / всех по студенту / всех по курсу сделано
    */

    // все записи на курсы
    protected static void getAllEnrollments(Connection connection) throws SQLException {
        
        Statement statement = connection.createStatement(); // создаем оператор для запроса (без параметров)
        ResultSet rs = statement.executeQuery("SELECT * FROM enrollments;");
        // выполняем запроса на поиск и получаем список ответов в ResultSet rs

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
            
                if (i != count) { param += " | "; }
            }

            System.out.println(param);
        }

        statement.close();
        System.out.println();
    }

    // поиск записей на курсы определенного студента (по айди студента)
    protected static void getEnrollmentsByStudentId(Connection connection, int id_student) throws SQLException {
        if (connection == null || connection.isClosed()) return; 

        if (id_student < 0) return;

        String param;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM enrollments " +
                        "WHERE id_student = ?;");              // создаем оператор шаблонного-запроса с "включаемыми" параметрами - ?
        statement.setInt(1, id_student);     // "безопасное" добавление параметров в запрос; с учетом их типа и порядка (индексация с 1)
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

    // добавление записи на курс (текущая дата)
    protected static void addEnrollment(Connection connection, int id_student, int id_course, String grade) throws SQLException {
        if (connection == null || connection.isClosed()) return;
        if (grade == null || grade.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO enrollments(id_student, id_course, grade) VALUES (?, ?, ?);"
        );
        statement.setInt(1, id_student);
        statement.setInt(2, id_course);
        statement.setString(3, grade);

        int count = statement.executeUpdate();
        System.out.println("INSERTed " + count + " enrollments");

        statement.close();
    }

    // добавление записи на курс (вводимая дата)
    protected static void addEnrollmentWithData(Connection connection, int id_student, int id_course, String grade, Date start_date) throws SQLException {
        if (connection == null || connection.isClosed()) return;
        if (grade == null || grade.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO enrollments(id_student, id_course, grade, start_date) VALUES (?, ?, ?, ?);"
        );
        statement.setInt(1, id_student);
        statement.setInt(2, id_course);
        statement.setString(3, grade);
        statement.setDate(4, start_date);

        int count = statement.executeUpdate();
        System.out.println("INSERTed " + count + " enrollments");

        statement.close();
    }

    // изменение оценки за курс (по айди студента и айди курса)
    protected static void correctEnrollmentGradeById(Connection connection, int id_student, int id_course, String grade) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        if (grade == null || grade.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE enrollments " +
                        "SET grade = ? " +
                        "WHERE id_student = ? AND id_course = ? " +
                        "RETURNING *", Statement.RETURN_GENERATED_KEYS
        );

        statement.setString(1, grade);
        statement.setInt(2, id_student);
        statement.setInt(3, id_course);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("UPDATed " + count + " enrollments");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getInt(2) +
                    " | " + rs.getString(3) + " | " + rs.getDate(4));
        }

        statement.close();
    }

    // удаление записи на курс (по айди студента и айди курса)
    protected static void deleteEnrolmentByIdStudentAndIdCourse(Connection connection, int id_student, int id_course) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM enrollments WHERE id_student = ? AND id_course = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );

        statement.setInt(1, id_student);
        statement.setInt(2, id_course);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("DELETEd " + count + " enrollments");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getInt(2) +
                    " | " + rs.getString(3) + " | " + rs.getDate(4));
        }

        statement.close();

    }

    // удаление всех записей студентов на определенный курс (по айди курса)
    protected static void deleteEnrolmentByIdCourse(Connection connection, int id_course) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM enrollments WHERE id_course = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, id_course);
        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("DELETEd " + count + " enrollments");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getInt(2) +
                    " | " + rs.getString(3) + " | " + rs.getDate(4));
        }
        statement.close();
    }

    // удаление всех записей определенного студента на курсы (по айди студента)
    protected static void deleteEnrolmentByIdStudent(Connection connection, int id_student) throws SQLException {
        if (connection == null || connection.isClosed()) return;

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM enrollments WHERE id_student = ? RETURNING *;", Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, id_student);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        System.out.println("DELETEd " + count + " enrollments");

        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getInt(2) +
                    " | " + rs.getString(3) + " | " + rs.getDate(4));
        }

        statement.close();
    }
}
