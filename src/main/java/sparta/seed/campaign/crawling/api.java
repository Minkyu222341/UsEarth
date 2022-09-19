package sparta.seed.campaign.crawling;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class api {
	public static void main(String[] args) {

StringBuilder result = new StringBuilder();
try {

	  String serviceUrl = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureLIst?serviceKey=";
		String serviceKey = "";
		String returnType = "json";
		int numOfRows = 100;
		int pageNo = 1;
		String itemCode = "PM10"; // CO, O3, PM10, PM2.5 == CO일산화탄소, O3오존
		String dataGubun = "HOUR";

		String urlstr = serviceUrl+serviceKey
		+"&returnType="+returnType+"&numOfRows="+numOfRows
				+"&pageNo="+pageNo+"&itemCode="+itemCode+"&dataGubun="+dataGubun;

		String zzz = "https://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=8b6987e3437f2f606c0b32a837986a81&svcType=api&svcCode=SCHOOL&contentType=json&gubun=high_list&perPage=2378&searchSchulNm=%EA%B3%A0%EB%93%B1%ED%95%99%EA%B5%90";


	URL url = new URL(urlstr);
	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	urlConnection.setRequestMethod("GET");

	BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

	String returnLine;

	while ((returnLine = br.readLine()) != null){
		result.append(returnLine + "\n");
	}
	urlConnection.disconnect();

} catch (Exception e) {
	e.printStackTrace();
}
		JSONObject rjson = new JSONObject(result.toString());
//		System.out.println(rjson);
		JSONArray jsonArray = rjson.getJSONObject("dataSearch").getJSONArray("content");

//		System.out.println(jsonArray);

		for (int i = 0; i < jsonArray.length(); i++) {
			System.out.println(jsonArray.getJSONObject(i).get("schoolName"));
		}


//		System.out.println(rjson.getJSONObject("response"));
//		System.out.println(rjson.getJSONObject("response").getJSONObject("body"));
//
//		System.out.println("---------------");
//		JSONArray jsonArray = rjson.getJSONObject("response").getJSONObject("body").getJSONArray("items");
//
//		for (int i = 0; i < jsonArray.length(); i++) {
//			System.out.println(jsonArray.getJSONObject(i));
//			System.out.println("-----");
		}
		// 6개 -> 6
//	}
}