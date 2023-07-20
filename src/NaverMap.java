

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NaverMap implements ActionListener {
    String fileAddress=null;
    PSearch naverMap;
    CSVReader csvList;
    String str = null;

    public NaverMap(PSearch naverMap) {
        this.naverMap = naverMap;
    }
    public NaverMap(CSVReader csvList, String str) {
        this.str = str;
        this.csvList = csvList;
        getMap();
    }

    public void getMap() {
        String clientId = "34hai0l91j";
        String clientSecret = "Ob7sZtZ7WsBas3ToMz9oOqVkO1fqI8ui40W7WkaN";
        AddressVO vo = null;

        try {
            String address = str;

            //naverMap.address.getText();
            //이곳에 검색할 주소를 넣는다.
            String addr = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            //System.out.println(object);

            JSONArray arr = object.getJSONArray("addresses");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                vo = new AddressVO();
                vo.setRoadAddress((String) temp.get("roadAddress"));
                vo.setJibunAddress((String)temp.get("jibunAddress"));
                vo.setX((String)temp.get("x"));
                vo.setY((String)temp.get("y"));
                //System.out.println(vo);
            }

            map_service(vo);

        } catch (Exception err) {
            System.out.println(err);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String clientId = "34hai0l91j";
        String clientSecret = "Ob7sZtZ7WsBas3ToMz9oOqVkO1fqI8ui40W7WkaN";
        AddressVO vo = null;

        try {
            String address = "부산광역시 강서구";

            //naverMap.address.getText();
            //이곳에 검색할 주소를 넣는다.
            String addr = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            System.out.println(object);

            JSONArray arr = object.getJSONArray("addresses");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                vo = new AddressVO();
                vo.setRoadAddress((String) temp.get("roadAddress"));
                vo.setJibunAddress((String)temp.get("jibunAddress"));
                vo.setX((String)temp.get("x"));
                vo.setY((String)temp.get("y"));
                System.out.println(vo);
            }

            map_service(vo);

        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public String map_service(AddressVO vo) {
        String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";

        try {
            String pos = URLEncoder.encode(vo.getX() + " " + vo.getY(), "UTF-8");
            URL_STATICMAP += "center=" + vo.getX() + "," + vo.getY();
            URL_STATICMAP += "&level=16&w=700&h=500";
            URL_STATICMAP += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(vo.getRoadAddress(), "UTF-8");

            URL url = new URL(URL_STATICMAP);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "34hai0l91j");
            con.setRequestProperty("X-NCP-APIGW-API-KEY", "Ob7sZtZ7WsBas3ToMz9oOqVkO1fqI8ui40W7WkaN");

            int responseCode = con.getResponseCode();

            // 정상호출인 경우.
            if (responseCode == 200) {
                InputStream is = con.getInputStream();

                int read = 0;
                byte[] bytes = new byte[1024];

                // 랜덤 파일명으로 파일 생성
                String tempName = Long.valueOf(new Date().getTime()).toString();
                File file = new File(tempName + ".jpg");	// 파일 생성.
                fileAddress = tempName+".jpg";
                file.createNewFile();

                OutputStream out = new FileOutputStream(file);

                while ((read = is.read(bytes)) != -1) {
                    out.write(bytes, 0, read);	// 파일 작성
                }

                is.close();
                out.close();
            } else {
                System.out.println(responseCode);
            }

        } catch(Exception e) {
            System.out.println(e);
        }
        return fileAddress;
    }

    public String getAddress() {
        return fileAddress;
    }
}
