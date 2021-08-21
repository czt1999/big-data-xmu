import java.sql.*;

public class TaskMySQL {

    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.3.9:3306/reader_home?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
        String user = "root";
        String password = ""; // Your password

        // The driver `com.mysql.cj.jdbc.Driver` will be automatically loaded by SPI

        // You may have to manually grant privileges of MySQL for remote access by the script below
        //
        // $ grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
        // $ flush privileges;
        //
        // Moreover, configure bind-address => 0.0.0.0

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            // 1:
            String sql_1 = "INSERT INTO `t_student` VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql_1);
            preparedStatement.setNull(1, Types.INTEGER);
            preparedStatement.setString(2, "scofield");
            preparedStatement.setInt(3, 45);
            preparedStatement.setInt(4, 89);
            preparedStatement.setInt(5, 100);
            System.out.println("SQL_1 >> " + (1 == preparedStatement.executeUpdate() ? "SUCCESS" : "FAILED"));
            // 2:
            String sql_2 = "SELECT `english` FROM `t_student` WHERE `student_name` = ?";
            preparedStatement = connection.prepareStatement(sql_2);
            preparedStatement.setString(1, "scofield");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("SQL_2 >> " + resultSet.getString("english"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != resultSet) {
                    resultSet.close();
                }
                if (null != preparedStatement) {
                    preparedStatement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
