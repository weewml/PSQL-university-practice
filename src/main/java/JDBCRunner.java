import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCRunner {
    // главный класс для запуска с реализованным консольным приложением

    private static final String DRIVER = "org.postgresql.Driver";

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String URL_LOCATE = "localhost/";
    private static final String DATABASE_NAME = "university";
    private static final String DATABASE_URL = PROTOCOL + URL_LOCATE + DATABASE_NAME;

    private static final String USER_NAME = "postgres";
    private static final String DATABASE_PASS = "postgres";

    public static void main(String[] args) {

        // проверка на подключение драйвера и бд
        if (checkDriver() && checkBD()) {
            System.out.println("Успешное подключение к базе данных | " + DATABASE_URL + "\n");
        } else return;

        boolean flag = Boolean.TRUE;

        while (flag) {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {

                Scanner sc = new Scanner(System.in);

                Menu.getMainMenu();
                int choice = sc.nextInt();
                System.out.println();

                switch (choice) {
                    case 1: // вывод всего
                        Menu.getMenu1();
                        int choice1 = sc.nextInt();
                        System.out.println();

                        switch (choice1) {
                            case 1:
                                Students.getAllStudents(connection);
                                break;
                            case 2:
                                Professors.getAllProfessors(connection);
                                break;
                            case 3:
                                Courses.getAllCourses(connection);
                                break;
                            case 4:
                                Enrollments.getAllEnrollments(connection);
                                break;
                            default:
                                System.out.println("Неправильный выбор, попробуйте еще раз");
                        }
                        break;

                    case 2: // поиск по таблице
                        Menu.getMenu2();
                        choice1 = sc.nextInt();
                        System.out.println();

                        switch (choice1) {
                            case 1: // поиск студента
                                System.out.print("Введите имя студента: ");
                                sc.nextLine();
                                String input = sc.nextLine();
                                Students.getStudentsByName(connection, input);
                                break;

                            case 2: // поиск профессора
                                System.out.print("Введите имя профессора: ");
                                sc.nextLine();
                                input = sc.nextLine();
                                Professors.getProfessorsByName(connection, input);
                                break;

                            case 3: // поиск курса
                                System.out.print("Введите название курса: ");
                                sc.nextLine();
                                input = sc.nextLine();
                                Courses.getCoursesByTitle(connection, input);
                                break;

                            case 4: // поиск записи на курс
                                System.out.print("Введите айди студента: ");
                                sc.nextLine();
                                int input1 = sc.nextInt();
                                Enrollments.getEnrollmentsByStudentId(connection, input1);
                                break;

                            default:
                                System.out.println("Неправильный выбор, попробуйте еще раз");
                        }
                        break;

                    case 3: // добавление в таблицу
                        Menu.getMenu3();
                        choice1 = sc.nextInt();
                        System.out.println();

                        switch (choice1) {
                            case 1: // добавление студента
                                System.out.print("Фамилия и имя студента: ");
                                sc.nextLine();
                                String full_name = sc.nextLine();
                                System.out.print("Группа студента: ");
                                String group_name = sc.nextLine();
                                System.out.print(
                                        "1 - Бакалавриат" +
                                        "\n2 - Специалитет" +
                                        "\n3 - Магистратура" +
                                        "\n4 - Аспирантура" +
                                        "\nУровень образования (цифрой):");
                                String education_level = "";
                                int count = Integer.parseInt(sc.nextLine());
                                education_level = switch (count) {
                                    case 1 -> "Бакалавриат";
                                    case 2 -> "Специалитет";
                                    case 3 -> "Магистратура";
                                    case 4 -> "Аспирантура";
                                    default -> education_level;
                                };
                                Students.addStudent(connection, full_name, group_name, education_level);
                                break;

                            case 2: // добавление профессора
                                System.out.print("ФИО профессора: ");
                                sc.nextLine();
                                full_name = sc.nextLine();
                                System.out.print("Кафедра: ");
                                String department= sc.nextLine();
                                Professors.addProfessor(connection, full_name, department);
                                break;

                            case 3: // добавление курса
                                System.out.print("Название курса: ");
                                sc.nextLine();
                                String title = sc.nextLine();
                                System.out.print("Айди профессора: ");
                                int id_professor = Integer.parseInt(sc.nextLine());
                                Courses.addCourse(connection, title, id_professor);
                                break;

                            case 4: // добавление записи на курс (текущая дата)
                                System.out.print("Айди студента: ");
                                sc.nextLine();
                                int id_student = Integer.parseInt(sc.nextLine());
                                System.out.print("Айди курса: ");
                                int id_course = Integer.parseInt(sc.nextLine());
                                System.out.print(
                                        "1 - Отлично" +
                                                "\n2 - Хорошо" +
                                                "\n3 - Удовлетворительно" +
                                                "\n4 - Неудовлетворительно" +
                                                "\nОценка (выберите цифру):");
                                String grade = "";
                                int count1 = Integer.parseInt(sc.nextLine());
                                grade = switch (count1) {
                                    case 1 -> "Отлично";
                                    case 2 -> "Хорошо";
                                    case 3 -> "Удовлетворительно";
                                    case 4 -> "Неудовлетворительно";
                                    default -> grade;
                                };
                                Enrollments.addEnrollment(connection, id_student, id_course, grade);
                                break;

                            case 5: // добавление записи на курс (своя дата)
                                System.out.print("Айди студента: ");
                                sc.nextLine();
                                int id_student1 = Integer.parseInt(sc.nextLine());
                                System.out.print("Айди курса: ");
                                int id_course1 = Integer.parseInt(sc.nextLine());
                                System.out.print(
                                        "1 - Отлично" +
                                                "\n2 - Хорошо" +
                                                "\n3 - Удовлетворительно" +
                                                "\n4 - Неудовлетворительно" +
                                                "\nОценка (выберите цифру):");
                                String grade1 = "";
                                int count2 = Integer.parseInt(sc.nextLine());
                                grade1 = switch (count2) {
                                    case 1 -> "Отлично";
                                    case 2 -> "Хорошо";
                                    case 3 -> "Удовлетворительно";
                                    case 4 -> "Неудовлетворительно";
                                    default -> grade1;
                                };
                                System.out.print("Введите дату в формате ГГГГ-ММ-ДД: ");
                                Date start_date = Date.valueOf(sc.nextLine());

                                Enrollments.addEnrollmentWithData(connection,
                                        id_student1, id_course1, grade1, start_date);
                                break;

                            default:
                                System.out.println("Неправильный выбор, попробуйте еще раз");
                        }
                        break;

                    case 4: // изменение записи в таблице
                        Menu.getMenu4();
                        choice1 = sc.nextInt();
                        System.out.println();

                        switch (choice1) {
                            case 1: // изменение группы и уровня образования студента
                                System.out.print("Айди студента: ");
                                sc.nextLine();
                                int id_student = Integer.parseInt(sc.nextLine());
                                System.out.print("Группа студента: ");
                                String group_name = sc.nextLine();
                                System.out.print(
                                        "1 - Бакалавриат" +
                                                "\n2 - Специалитет" +
                                                "\n3 - Магистратура" +
                                                "\n4 - Аспирантура" +
                                                "\nУровень образования (цифрой):");
                                String education_level = "";
                                int count = Integer.parseInt(sc.nextLine());
                                education_level = switch (count) {
                                    case 1 -> "Бакалавриат";
                                    case 2 -> "Специалитет";
                                    case 3 -> "Магистратура";
                                    case 4 -> "Аспирантура";
                                    default -> education_level;
                                };
                                Students.correctStudentById(connection, id_student, group_name, education_level);
                                break;

                            case 2: // изменение кафедры профессора
                                System.out.print("Айди профессора: ");
                                sc.nextLine();
                                int id_professor = Integer.parseInt(sc.nextLine());
                                System.out.print("Кафедра профессора: ");
                                String department = sc.nextLine();

                                Professors.correctProfessor(connection, id_professor, department);
                                break;

                            case 3: // изменение курса
                                System.out.print("Введите айди курса: ");
                                sc.nextLine();
                                int id_course = Integer.parseInt(sc.nextLine());

                                System.out.print("Введите новое название: ");
                                String title = sc.nextLine();

                                Courses.correctCourseById(connection, id_course, title);
                                break;

                            case 4: // изменение оценки за курс
                                System.out.print("Айди студента: ");
                                sc.nextLine();
                                int id_student1 = Integer.parseInt(sc.nextLine());
                                System.out.print("Айди курса: ");
                                int id_course1 = Integer.parseInt(sc.nextLine());
                                System.out.print(
                                        "1 - Отлично" +
                                                "\n2 - Хорошо" +
                                                "\n3 - Удовлетворительно" +
                                                "\n4 - Неудовлетворительно" +
                                                "\nОценка (выберите цифру):");
                                String grade1 = "";
                                int count2 = Integer.parseInt(sc.nextLine());
                                grade1 = switch (count2) {
                                    case 1 -> "Отлично";
                                    case 2 -> "Хорошо";
                                    case 3 -> "Удовлетворительно";
                                    case 4 -> "Неудовлетворительно";
                                    default -> grade1;
                                };

                                Enrollments.correctEnrollmentGradeById(connection, id_student1, id_course1, grade1);
                                break;

                            default:
                                System.out.println("Неправильный выбор, попробуйте еще раз");
                        }
                        break;

                    case 5: // удаление записи в таблице
                        Menu.getMenu5();
                        choice1 = sc.nextInt();
                        System.out.println();

                        switch (choice1) {
                            case 1: // удаление студента
                                System.out.print("Введите айди студента: ");
                                sc.nextLine();
                                int id_student = Integer.parseInt(sc.nextLine());
                                Students.deleteStudentById(connection, id_student);
                                break;

                            case 2: // удаление профессора
                                System.out.print("Введите айди профессора: ");
                                sc.nextLine();
                                int id_professor = Integer.parseInt(sc.nextLine());
                                Professors.deleteProfessorById(connection, id_professor);
                                break;

                            case 3: // удаление курса по айди
                                System.out.print("Введите айди курса: ");
                                sc.nextLine();
                                int id_course = Integer.parseInt(sc.nextLine());
                                Courses.deleteCourseById(connection, id_course);
                                break;

                            case 4: // удаление курсов от определенного преподавателя
                                System.out.print("Введите айди преподавателя: ");
                                sc.nextLine();
                                id_professor = Integer.parseInt(sc.nextLine());
                                Courses.deleteCoursesByIdProfessor(connection, id_professor);
                                break;

                            case 5: // удаление записи по айди студента и курса
                                System.out.print("Введите айди студента: ");
                                sc.nextLine();
                                id_student = Integer.parseInt(sc.nextLine());
                                System.out.print("Введите айди курса: ");
                                id_course = Integer.parseInt(sc.nextLine());
                                Enrollments.deleteEnrolmentByIdStudentAndIdCourse(connection, id_student, id_course);
                                break;

                            case 6: // удаление всех записей на определенный курс
                                System.out.print("Введите айди курса: ");
                                sc.nextLine();
                                id_course = Integer.parseInt(sc.nextLine());
                                Enrollments.deleteEnrolmentByIdCourse(connection, id_course);
                                break;

                            case 7: // удаление всех записей определенного студента
                                System.out.print("Введите айди студента: ");
                                sc.nextLine();
                                id_student = Integer.parseInt(sc.nextLine());
                                Enrollments.deleteEnrolmentByIdStudent(connection, id_student);
                                break;

                            default:
                                System.out.println("Неправильный выбор, попробуйте еще раз");
                        }
                        break;

                    case 6: // завершение программы с главного меню
                        flag = Boolean.FALSE;
                        break;

                    default:
                        System.out.println("Неправильный ввод, попробуйте еще раз");
                }

                System.out.println();

                if (flag){ // завершение программы или вывод меню после полученных данных
                    System.out.print("1 - Меню, 2 - Выход: ");
                    int input = sc.nextInt();
                    if (input == 2) {
                        flag = Boolean.FALSE;
                    }
                    System.out.println();
                }

            } catch (SQLException e) { // ошибки с бд
                if (e.getSQLState().startsWith("23")) {
                    System.out.println("\nПроизошло дублирование данных\n");
                } else throw new RuntimeException(e);
            } catch (RuntimeException e) { // ошибки с парсинг
                System.out.println("Вы должны вводить числа! Попробуйте еще раз\n");
            }
        }
    }

    // проверка подключения драйвера
    public static boolean checkDriver () {
        try {
            Class.forName(DRIVER);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера!");
            throw new RuntimeException(e);
        }
    }

    // проверка подключения базы данных
    public static boolean checkBD () {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Нет подключения к базе данных!");
            throw new RuntimeException(e);
        }
    }
}