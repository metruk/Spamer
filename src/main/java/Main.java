import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main {
	static Parser parser = new Parser();
	final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	private static Map<String, String> todayNewsTitles = new HashMap<>();
	private static Logger logger = Logger.getLogger(Main.class.getName());


	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		//logger.
		//MysqlDAO mysql = new MysqlDAO();
		//todayNewsTitles = todayNewsTitles = mysql.selectTodayNews();
		final Main m = new Main();
		final ScheduledExecutorService ses = Executors
				.newScheduledThreadPool(5);
		System.out.println("Enter a token");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		final String token = reader.readLine();
		Runnable pinger1 = null;
		try{
		//final String token="53789dc511572e631dc295ce1e5a6b73d0a49a11e1579cb2361d20e7a539e026d01343c4aaf5527ed7f06";
		 pinger1 = new Runnable() {
			public void run() {
				try {
					m.mainMagic(parser, token);
				} catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("1.Interner connection failed");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("2.Interner connection failed");
					}
				}

			}
		};
		}catch(Exception ex){
			
		}
		ses.scheduleWithFixedDelay(pinger1, 1, 15, TimeUnit.SECONDS);
	}

	public void mainMagic(Parser parser, String token) throws Exception {

		int currentTime = DateService.currentHoursMinutesFormat();
		MysqlDAO mysql = new MysqlDAO();
		todayNewsTitles = todayNewsTitles =mysql.selectTodayNews();
		/*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));		
		String number = in.readLine();
		System.out.println(number);
		todayNewsTitles.put("03.07,22-00 Трансляция матча Франция – Исландия. Чемпионат Европы 2016. Смотреть онлайн", "03-07-22-00-online-francia-islandia-tournament-evropi-2016/");*/
		System.err.println(todayNewsTitles);
		
		for (Map.Entry<String, String> entry : todayNewsTitles.entrySet()) {
			String header = entry.getKey();
			System.out.println(header);
			int newsTime = DateService.newsCurrentHoursMinutesFormat(header);
			System.out.println("news " + newsTime + "header " + header);
			System.out.println("current" + currentTime);
			// note!
			int beforeGameStart=10;
			int afterGameStart=20+20;
			int afterFirstHalfStart=45+15;
			int afterSecondHalf=70+20; //95
			
			if (((currentTime + beforeGameStart > newsTime) && (currentTime <= newsTime + afterGameStart)) || ((currentTime >= newsTime + afterFirstHalfStart) && (currentTime <= newsTime + afterSecondHalf))) {
				/*Do d = new Do();
				MysqlDAO sql = new MysqlDAO();
				Service service = new Service();
				StreamsRobber robber = new StreamsRobber();*/
			//	d.updater(sql, service, robber); 
				//if(currentTime!=newsTime){
			//	System.out.println("Translation header" + header);
				
				// href to post in comment
				//MODIFIED HREF BLOCK
				//!!!!!!!!
				String translationHref = "http://www.footlivehd1.ru/";
				translationHref += entry.getValue();
				System.out.println("HREF"+translationHref);
				
				
				// String tinyTranslation= Parser.getTinyUrl(translationHref);
				// System.out.println(tinyTranslation);
				//System.out.println(translationHref);
				 List<String> spamFinds = parser.getVkHref(header);;
			
			
			
				System.out.println("SPAM"+spamFinds);
				String commentPhoto = parser.getSpamPhoto(header);
			
				for (int spamHref = 0; spamHref < spamFinds.size(); spamHref++) {
					
					if (spamFinds.get(spamHref) != null) {
						List<String> groupForSpamIds = parser.groupsForSpamList(spamFinds
								.get(spamHref));
						System.err.println("spamids"+groupForSpamIds);
						System.out.println("spamfind "+spamFinds.get(spamHref));	
						List<String> spamWithTopGroups=parser.spamList(groupForSpamIds);
						parser.groupConnector(spamWithTopGroups, commentPhoto,
								translationHref, token);

					}
					
					//multitasked
				/*	MyThread t1=new MyThread(spamFinds.get(spamHref), translationHref, commentPhoto, token);
					new Thread(t1,"DED "+spamHref+" "+newsTime).start();*/
					
				}

			}

		}

	}
	static class MyThread implements Runnable{
		String translationHref;
		String groupFind;
		String commentPhoto;
		String token;
		MyThread(String groupFind,String translationHref,String commentPhoto,String token){
			this.groupFind=groupFind;
			this.translationHref=translationHref;
			this.commentPhoto=commentPhoto;
			this.token=token;
		}
		@Override
		public void run() {
			logger.info("Started");
			
			if (groupFind != null) {
				try{
				List<String> groupForSpamIds = parser.groupsForSpamList(groupFind);
				
				logger.info(groupForSpamIds);
				List<String> spamWithTopGroups=parser.spamList(groupForSpamIds);
				parser.groupConnector(spamWithTopGroups, commentPhoto,
						translationHref, token);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
	}
}
