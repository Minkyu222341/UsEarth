package sparta.seed.campaign.crawling;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sparta.seed.campaign.domain.AqApiData;
import sparta.seed.campaign.repository.AqApiDataRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class api {
	@Value("${openapi.serviceKey}")
	String serviceKey;
	private final AqApiDataRepository aqApiDataRepository;

	public void saveApiData(String itemCode) throws IOException {
		StringBuilder result = new StringBuilder();
		String urlstr = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureLIst?serviceKey="+serviceKey+"&returnType=json&numOfRows=100&pageNo=1&itemCode="+itemCode+"&dataGubun=HOUR";

	URL url = new URL(urlstr);
	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	urlConnection.setRequestMethod("GET");

	BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

	String returnLine;

	while ((returnLine = br.readLine()) != null){
		result.append(returnLine).append("\n");
	}
	urlConnection.disconnect();

		JSONObject rjson = new JSONObject(result.toString());

		JSONArray jsonArray = rjson.getJSONObject("response").getJSONObject("body").getJSONArray("items");
		String[] regionList = {"jeonbuk", "gyeonggi", "gangwon", "gwangju", "ulsan", "sejong", "chungbuk", "seoul",
				"gyeongnam", "chungnam", "daejeon", "busan", "gyeongbuk", "jeju", "daegu", "incheon", "jeonnam"};

		List<AqApiData> aqApiDataList = new ArrayList<>();
			for (String region : regionList) {
				AqApiData aqApiData = AqApiData.builder().category(itemCode).region(region).amount((String) jsonArray.getJSONObject(0).get(region)).build();
				aqApiData.setDatetime((String) jsonArray.getJSONObject(0).get("dataTime"));
				aqApiDataList.add(aqApiData);
			}
		aqApiDataRepository.saveAll(aqApiDataList);
	}
}