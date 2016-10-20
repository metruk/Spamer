import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
	final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	static Map<String, String> groupAndPostId = new HashMap<String, String>();
	Captcha cap = new Captcha();
	private static Logger logger = Logger.getLogger(Parser.class.getName());
	private static int GROUP_SPAM_MIN_BORDER_FOLLOWERS=35000;
	private static int GROUP_SPAM_MAX_BORDER_FOLLOWERS=1000000;
	public void groupConnector(List<String> groupHref, String commentPhoto,
			String tinyHref, String apiToken) throws Exception {
		String banStatus = "";
		logger.info("gr" + groupHref);
		for (int curGroup = 0; curGroup < groupHref.size(); curGroup++) {
			Document doc = null;
			while (doc == null) {
				try {
					doc = Jsoup.connect(groupHref.get(curGroup)).timeout(700)
							.userAgent(USER_AGENT).get();
				} catch (Exception he) {
					he.printStackTrace();
				}
			}
			Elements elements = doc.select("div[class=wall_text]");
			Elements id = elements.select("div[id]");
			if(!doc.toString().contains("page_verified")){	
	
			int amountOfSpamNews = 2;
			String postID = "";
			banStatus = "";
			for (int postCounter = 1; postCounter <= amountOfSpamNews; postCounter++) {
				if (banStatus.contains("Access to status replies denied")) {
					logger.info("next");
					break;
				}
				try {
					Element text = elements.get(postCounter).select("div")
							.get(1);
					postID = text.attr("id");
					logger.info(postID);
				} catch (java.lang.IndexOutOfBoundsException ex) {
					break;
				}
				groupAndPostId = groupAndPostId(postID);

				for (Map.Entry<String, String> entry : groupAndPostId
						.entrySet()) {
					String ownerId = entry.getKey();
					String postId = entry.getValue();
					// !!
					int amountSpamComments=2;
					for (int i = 0; i < amountSpamComments; i++) {
						if (banStatus.contains("Access to status replies denied")) {
							break;
						}
						System.out.println("Group "+groupHref.get(curGroup));
						banStatus = makeAComment(ownerId, postId, tinyHref,
								commentPhoto, apiToken);
						Thread.sleep(1500);

					}

					// should be break if banned

					logger.info("оставляю комент");
				}

			}
			
		}

		}
		// note!
		// return id;
	}

	public List<String> getVkHref(String header) {
		HashMap<String, String> headerAndHref = new HashMap<>();
		List<String> list = new ArrayList<String>();
		String vkHref = null;
		headerAndHref.put("Чемпионат Европы","http://vk.com/search?c[q]=%D0%B5%D0%B2%D1%80%D0%BE%202016&c[section]=communities");
		headerAndHref.put("Челси","http://vk.com/search?c[q]=%D1%87%D0%B5%D0%BB%D1%81%D0%B8&c[section]=communities");
		headerAndHref.put("Арсенал","http://vk.com/search?c[q]=arsenal&c[section]=communities");
		headerAndHref.put("Манчестер Юнайтед","http://vk.com/search?c[q]=%D0%BC%D0%B0%D0%BD%D1%87%D0%B5%D1%81%D1%82%D0%B5%D1%80%20%D1%8E%D0%BD%D0%B0%D0%B9%D1%82%D0%B5%D0%B4&c[section]=communities");
		headerAndHref.put("Манчестер Сити","http://vk.com/search?c[q]=%D0%BC%D0%B0%D0%BD%D1%87%D0%B5%D1%81%D1%82%D0%B5%D1%80%20%D1%81%D0%B8%D1%82%D0%B8&c[section]=communities");
		headerAndHref.put("Ливерпуль","http://vk.com/search?c[q]=%D0%BB%D0%B8%D0%B2%D0%B5%D1%80%D0%BF%D1%83%D0%BB%D1%8C&c[section]=communities");
		headerAndHref.put("Реал Мадрид","http://vk.com/search?c[q]=%D1%80%D0%B5%D0%B0%D0%BB%20%D0%BC%D0%B0%D0%B4%D1%80%D0%B8%D0%B4&c[section]=communities");
		headerAndHref.put("Атлетико Мадрид","http://vk.com/search?c[q]=%D0%B0%D1%82%D0%BB%D0%B5%D1%82%D0%B8%D0%BA%D0%BE%20%D0%BC%D0%B0%D0%B4%D1%80%D0%B8%D0%B4&c[section]=communities");
		headerAndHref.put("Барселона","http://vk.com/search?c[q]=%D0%91%D0%B0%D1%80%D1%81%D0%B5%D0%BB%D0%BE%D0%BD%D0%B0&c[section]=communities");
		headerAndHref.put("Милан","http://vk.com/search?c[q]=ac%20milan&c[section]=communities");
		headerAndHref.put("Ювентус","http://vk.com/search?c[q]=%D1%8E%D0%B2%D0%B5%D0%BD%D1%82%D1%83%D1%81&c[section]=communities");
		headerAndHref.put("Бавария","http://vk.com/search?c[q]=bayern&c[section]=communities");
		headerAndHref.put("Боруссия Дортмунд","http://vk.com/search?c[q]=%D0%91%D0%BE%D1%80%D1%83%D1%81%D1%81%D0%B8%D1%8F%20%D0%94%D0%BE%D1%80%D1%82%D0%BC%D1%83%D0%BD%D0%B4&c[section]=communities");
		headerAndHref.put("Зенит","http://vk.com/search?c[q]=%D0%B7%D0%B5%D0%BD%D0%B8%D1%82%20%D1%84%D0%BA&c[section]=communities");
		headerAndHref.put("Локомотив","http://vk.com/search?c[q]=%D0%BB%D0%BE%D0%BA%D0%BE%D0%BC%D0%BE%D1%82%D0%B8%D0%B2&c[section]=communities");
		headerAndHref.put("ЦСКА","http://vk.com/search?c[q]=%D0%BF%D1%84%D0%BA%20%D0%A6%D0%A1%D0%9A%D0%90%20&c[section]=communities");
		headerAndHref.put("Спартак","http://vk.com/search?c[q]=%D1%81%D0%BF%D0%B0%D1%80%D1%82%D0%B0%D0%BA%20%D1%84%D0%BA&c[section]=communities");
		//headerAndHref.put("Рубин","http://vk.com/search?c[q]=%D1%80%D1%83%D0%B1%D0%B8%D0%BD%20%D1%84%D0%BA&c[section]=communities");
		headerAndHref.put("Анжи","http://vk.com/search?c[q]=%D1%84%D0%BA%20%D0%90%D0%BD%D0%B6%D0%B8&c[section]=communities");
		
			for (Map.Entry<String, String> entry : headerAndHref.entrySet()) {
			if (header.contains(entry.getKey())) {
				vkHref = entry.getValue();
				list.add(vkHref);
			}
		}
		return list;

	}

	public String getSpamPhoto(String header) throws IOException {
		String photo = "";
		if (header.contains("Лига Европы")) {
			photo = "photo-50978521_401315494";

		} else if (header.contains("Лига Чемпионов")) {
			photo = "photo-50978521_401315493";

		} else if (header.contains("Чемпионат Украины")) {
			photo = "photo-50978521_401315495";

		} else if (header.contains("Чемпионат Испании")) {
			photo = "photo-50978521_401315529";

		} else if (header.contains("Кубок Украины")) {
			photo = "photo-50978521_401315496";

		} else if (header.contains("Кубок Испании")) {
			photo = "photo-50978521_401315498";

		} else if (header.contains("Чемпионат России. Премьер-Лига")) {
			photo = "photo-50978521_401315513";

		} else if (header.contains("Чемпионат Италии")) {
			photo = "photo-50978521_401315521";

		} else if (header.contains("Кубок Италии")) {
			photo = "photo-50978521_401315499";

		} else if (header.contains("Кубок Германии")) {
			photo = "photo-50978521_401315503";

		} else if (header.contains("Чемпионат Гeрмании")) {
			photo = "photo-50978521_401315509";

		} else if (header.contains("Чемпионат Франции")) {
			photo = "photo-50978521_401315518";

		} else if (header.contains("Кубок Франции")) {
			photo = "photo-50978521_401316348";

		} else if (header.contains("Кубок Французской Лиги")) {
			photo = "photo-50978521_401330883";

		} else if (header.contains("Чемпионат Англии. Премьер-Лига")) {
			photo = "photo-50978521_401315525";

		} else if (header.contains("Кубок Английской Лиги")) {
			photo = "photo-50978521_401315505";

		} else if (header.contains("РФПЛ")) {
			photo = "photo-50978521_401315513";

		} else if (header.contains("Кубок Англии")) {
			photo = "photo-50978521_401315506";

		} else if (header.contains("Кубок России")) {
			photo = "photo-50978521_401331831";

		}  else if (header.contains("2016")) {
			photo = "photo-50978521_414930685";

		}  else if (header.contains("Квалификация")) {
			photo = "photo-50978521_401315493";

		}  

		else {
			photo = "photo-50978521_422007693";
		}
		return photo;
	}

	public List<String> groupsForSpamList(String spamFindHref)
			throws IOException {
		ArrayList<String> groupsForSpam = new ArrayList<>();
		Document doc = null;
		while (doc == null) {
			try {
				doc = Jsoup.connect(spamFindHref).timeout(700)
						.userAgent(USER_AGENT).get();
			} catch (java.net.UnknownHostException he) {
				he.printStackTrace();
			} catch (java.net.SocketTimeoutException he) {
				he.printStackTrace();
				
			}
		}
		
		Elements elements = doc.select("div[class=search_results search_communities_results page_block]");
		Elements elemen=elements.select("div[class=groups_row search_row clear_fix]");
		for (int i = 0; i < elemen.size(); i++) {
			Elements link = elemen.get(i).select("a[href]");
			String href = link.attr("abs:href");
			//!! group id cut
			String cuttedHref=href.substring(15, href.length());
			/*System.out.println(href);
			System.out.println("Cuted href "+cuttedHref);*/
			int groupAmount=groupMembers(cuttedHref);
			if(groupAmount>=GROUP_SPAM_MIN_BORDER_FOLLOWERS){
		
				groupsForSpam.add(href);
			}

			/*groupsForSpam.add(href);
			if (groupsForSpam.size() ==2 ) {
				break;
			}*/
		}

		return groupsForSpam;
	}

	static Map<String, String> groupAndPostId(String newsId) {
		HashMap<String, String> groupAndPostId = new HashMap<String, String>();
		int indexOfSpace = newsId.indexOf("_");
		
		String groupId = "-" + newsId.substring(4, indexOfSpace);
		String postId = newsId.substring(indexOfSpace + 1, newsId.length());
		groupAndPostId.put(groupId, postId);
		return groupAndPostId;
	}

	private String makeAComment(String ownerId, String newsId,
			String textComment, String attachments, String apiToken)
			throws Exception {
		String captcha;
		System.out.println("making a comment...");
		 textComment=URLEncoder.encode("Смотрите ЗДЕСЬ!  &#9917; \n"+textComment);
		String isBanned = null;
		String requestUrl = "https://api.vk.com/method/wall.addComment?owner_id="
				+ ownerId
				+ "&post_id="
				+ newsId
				+ "&text="
				+ textComment
				+ "&attachments=" + attachments + "&access_token=" + apiToken;
	
		logger.info("request url: " + requestUrl);
		URL obj = new URL(requestUrl);
		BufferedReader in = null;
		HttpsURLConnection con = null;
		StringBuffer response = null;
		while (isBanned == null) {
			try {
				con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setDoOutput(true);
				con.setConnectTimeout(700);
				Thread.sleep(300);
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				isBanned = response.toString();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		captcha = isBanned;
		
		logger.info("response" + captcha);
		String forSidStr = captcha;
		if (captcha.contains("Captcha needed")) {
			String captchaFindStr = "captcha_img";
			int index = captcha.indexOf(captchaFindStr)
					+ captchaFindStr.length() + 3;
			captcha = captcha.substring(index, captcha.length() - 3);
			captcha = captcha.replace("\\", "");
			forSidStr = forSidStr.substring(index, forSidStr.length() - 3);
			int indexSecondSid = forSidStr.indexOf("sid");
			forSidStr = forSidStr.substring(indexSecondSid + 4);
			logger.info(forSidStr);
			logger.info(captcha);
			URL url = new URL(captcha);
			String capchaText = null;
			while (capchaText == null) {
				try {
					capchaText = cap.sendPost(url);
					logger.info("cap" + capchaText);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			logger.info("capText" + capchaText);

			makeACommentUsingCaptcha(ownerId, newsId, textComment, attachments,
					apiToken, forSidStr, capchaText);
		} else if (isBanned.toString().contains("Internal server error")) {
			makeAComment(ownerId, newsId, textComment, attachments, apiToken);
		}
		return isBanned;
	}

	private void makeACommentUsingCaptcha(String ownerId, String newsId,
			String textComment, String attachments, String apiToken,
			String sid, String captchaText) throws Exception {

		String requestUrl = "https://api.vk.com/method/wall.addComment?owner_id="
				+ ownerId
				+ "&post_id="
				+ newsId
				+ "&text="
				+ textComment
				+ "&attachments="
				+ attachments
				+ "&access_token="
				+ apiToken
				+ "&captcha_sid=" + sid + "&captcha_key=" + captchaText;
		String postStatus = null;
		int responseCode = 0;
		while (postStatus == null) {
			try {
				URL obj = new URL(requestUrl);
				HttpsURLConnection con = (HttpsURLConnection) obj
						.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setDoOutput(true);
				con.setConnectTimeout(1000);
				 responseCode = con.getResponseCode();
				logger.info("Sending 'GET' request to URL : "
						+ requestUrl);
				logger.info("Response Code : " + responseCode);
				Thread.sleep(250);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				postStatus = response.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		if (postStatus.contains("Internal server error")) {
			Thread.sleep(200);
			makeACommentUsingCaptcha(ownerId, newsId, textComment, attachments,
					apiToken, sid, captchaText);
		}else if(responseCode==400){
			Thread.sleep(200);
			makeACommentUsingCaptcha(ownerId, newsId, textComment, attachments,
					apiToken, sid, captchaText);
		}
	}

	public static String getTinyUrl(String fullUrl) throws HttpException,
			IOException {
		HttpClient httpclient = new HttpClient();
		// Prepare a request object
		HttpMethod method = new GetMethod("http://tinyurl.com/api-create.php");
		method.setQueryString(new NameValuePair[] { new NameValuePair("url",
				fullUrl) });
		httpclient.executeMethod(method);
		String tinyUrl = method.getResponseBodyAsString();
		method.releaseConnection();
		return tinyUrl;
	}

	/*private String getGroupId(String group) throws IOException {
		Document doc = null;
		while (doc == null) {
			try {
				doc = Jsoup.connect(group).timeout(700).userAgent(USER_AGENT)
						.get();
			} catch (java.net.UnknownHostException he) {
				he.printStackTrace();

			} catch (java.net.SocketTimeoutException he) {
				he.printStackTrace();
				doc = Jsoup.connect(group).timeout(700).userAgent(USER_AGENT)
						.get();
			}
		}
		Elements elements = doc.select("div[class=wall_text]");
		Elements id = elements.select("div[id]");
		String postID = id.attr("id");
		postID = postID.substring(4, postID.length());
		String groupId = postID.substring(0, postID.indexOf("_"));
		String followersPost = "http://vk.com/search?c[section]=people&c[group]="
				+ groupId;
		logger.info(followersPost);
		while (doc == null) {
			try {
				doc = Jsoup.connect(followersPost).timeout(700)
						.userAgent(USER_AGENT).get();
			} catch (java.net.UnknownHostException he) {
				he.printStackTrace();

			} catch (java.net.SocketTimeoutException he) {
				he.printStackTrace();
				doc = Jsoup.connect(group).timeout(700).userAgent(USER_AGENT)
						.get();
			}
		}

		Elements summary = doc.select("div[class=summary_wrap clear_fix]");

		return doc.toString();
	}*/

	List<String> spamList(List<String> list){
		List<String> topList= new ArrayList<>();
		if(list.size()>=4){
		for(int i=0;i<4;i++){
		topList.add(list.get(i));
		}
	
		for(int i=0;i<list.size();i++){
		if(i%(list.size()/3)==0 && i!=0){
		list.addAll(i,topList);
		i+=topList.size();
		}
	
		}
		}
	
		return list;
		}
	public int groupMembers(String groupId) throws MalformedURLException{
		String isBanned = null;
		
		String requestUrl = "https://api.vk.com/method/groups.getMembers?group_id="
				+ groupId
				+ "&sort=id_asc"
				+ "&offset=100"
				+ "&count=10" ;
		//logger.info("request url: " + requestUrl);
		URL obj = new URL(requestUrl);
		BufferedReader in = null;
		HttpsURLConnection con = null;
		StringBuffer response = null;
		while (isBanned == null) {
			try {
				con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setDoOutput(true);
				con.setConnectTimeout(700);
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				isBanned = response.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		String status = isBanned;
		
		int i=0;
		while (status.charAt(i)!=','){
			i++;
		}
		String groupIdAmount=status.substring(21,i);
		
		int amountUsers;
		if(groupIdAmount.contains("125")){
			amountUsers=-1;
		}else{
			
			 amountUsers=Integer.parseInt(groupIdAmount);
		}

		return  amountUsers;
		
	}
}