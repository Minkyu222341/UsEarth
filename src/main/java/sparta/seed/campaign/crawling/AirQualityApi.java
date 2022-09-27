package sparta.seed.campaign.crawling;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sparta.seed.campaign.domain.AqApiData;
import sparta.seed.campaign.repository.AqRepository;

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
public class AirQualityApi {
	@Value("${openapi.serviceKey}")
	String serviceKey;
	private final AqRepository aqApiDataRepository;

	public void saveApiData(int index) throws IOException,InterruptedException {
		String[] itemCodeList = {"co", "o3", "no2", "so2", "pm10", "pm25"};
		List<AqApiData> aqApiDataList = new ArrayList<>();
		for (String itemCode : itemCodeList) {

			StringBuilder result = new StringBuilder();
			String urlstr = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureLIst?serviceKey=" + serviceKey + "&returnType=json&numOfRows=100&pageNo=1&itemCode=" + itemCode + "&dataGubun=HOUR";

			URL url = new URL(urlstr);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

			String returnLine;

			while ((returnLine = br.readLine()) != null) {
				result.append(returnLine).append("\n");
			}

			Thread.sleep(10000);
			urlConnection.disconnect();
			JSONObject rjson = new JSONObject(result.toString());

			JSONArray jsonArray = rjson.getJSONObject("response").getJSONObject("body").getJSONArray("items");
			String[] regionList = {"jeonbuk", "gyeonggi", "gangwon", "gwangju", "ulsan", "sejong", "chungbuk", "seoul",
					"gyeongnam", "chungnam", "daejeon", "busan", "gyeongbuk", "jeju", "daegu", "incheon", "jeonnam"};

			for (String region : regionList) {
				AqApiData aqApiData = AqApiData.builder()
						.category(itemCode)
						.region(region)
						.datetime((String) jsonArray.getJSONObject(index).get("dataTime"))
						.amount(Double.parseDouble(String.valueOf(jsonArray.getJSONObject(index).get(region))))
						.build();
				aqApiDataList.add(aqApiData);
			}
		}
		aqApiDataRepository.saveAll(aqApiDataList);
	}
}