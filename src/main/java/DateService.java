import java.text.SimpleDateFormat;
import java.util.Date;


public class DateService {
	
	
	static int currentHoursMinutesFormat() {
		SimpleDateFormat dateMinutes = new SimpleDateFormat("mm");
		SimpleDateFormat dateHours = new SimpleDateFormat("hh");
		int currentMinutes=Integer.parseInt(dateMinutes.format(new Date()));
		int currentHour=Integer.parseInt(dateHours.format(new Date()));
			return (currentHour+12)*60+currentMinutes;
	}
	
	static int newsCurrentHoursMinutesFormat(String header) {
		
		int currentMinutes=Integer.parseInt(header.substring(9,11));
		System.out.println(currentMinutes);
		int currentHour=Integer.parseInt(header.substring(6,8));
			return currentHour*60+currentMinutes;
	}
	
	
	static String currentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(new Date());
	}
	
	static String currentDateTranslationFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
			return dateFormat.format(new Date());
	}
}
