import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    // 의도적 결함 1: SQL Injection 및 리소스 누수(Connection/Statement 미종료)
    public List<String> searchUsers(String keyword) {
        List<String> results = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM users WHERE username = ?");

            // 위험: 외부 입력을 그대로 문자열 결합하여 SQL Injection 취약점 발생
            stmt.setString(1, keyword);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("username"));
            }

            // rs, stmt, conn close() 누락으로 인한 DB 커넥션 락 발생 가능
        } catch (Exception e) {
            
        }
        return results;
    }

    // 의도적 결함 2: 0으로 나눌 때 발생하는 예외(ArithmeticException) 처리 누락
    public double calculateAverageScore(List<Integer> scores) {
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        // scores 리스트가 비어있을 경우 예외 발생
        return sum / scores.size();
    }
}