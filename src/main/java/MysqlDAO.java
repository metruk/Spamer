
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MysqlDAO {
	private static Connection connection;
	private static final String URL = "jdbc:mysql://hostx.mysql.ukraine.com.ua:3306/hostx_footlivehd?useUnicode=true&characterEncoding=UTF-8";
	private static final String USERNAME = "hostx_footlivehd";
	private static final String PASSWORD = "udxz56xp";
	
	
	MysqlDAO() throws SQLException  {
		connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
	

	public Connection getConnection() {
		return connection;
	}
	
	Map<String,String> selectTodayNews() throws SQLException {
		String selectTodayTranslation="SELECT post_title,post_name FROM wp_posts WHERE post_title LIKE '"+DateService.currentDateTranslationFormat()+"%' AND post_status='publish';";
		HashMap<String,String> todayTitleAndHref=new HashMap<>();
		PreparedStatement preparedStatement = null;
		preparedStatement = getConnection().prepareStatement(selectTodayTranslation);
		List<String> todayTitles= new ArrayList<>();
		ResultSet rs = preparedStatement.executeQuery();
		
			while (rs.next()) {
				String title = rs.getString(1);
				String postName = rs.getString(2);
				todayTitles.add(title);
				if(title.length()>10){
				todayTitleAndHref.put(title, postName);
				}
				
			}
		preparedStatement.execute();
				return todayTitleAndHref;
	}	
	
	Map<String,String> selectTodayNews(String after12) throws SQLException {
		String selectTodayTranslation="SELECT post_title,post_name FROM wp_posts WHERE post_title LIKE '"+DateService.currentDateTranslationFormat()+"%' AND post_status='publish';";
		HashMap<String,String> todayTitleAndHref=new HashMap<>();
		PreparedStatement preparedStatement = null;
		preparedStatement = getConnection().prepareStatement(selectTodayTranslation);
		List<String> todayTitles= new ArrayList<>();
		ResultSet rs = preparedStatement.executeQuery();
		
			while (rs.next()) {
				String title = rs.getString(1);
				String postName = rs.getString(2);
				todayTitles.add(title);
				if(title.length()>10){
				todayTitleAndHref.put(title, postName);
				}
				
			}
		preparedStatement.execute();
				return todayTitleAndHref;
	}	
	
}