package com.rest.api.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * Description : 공공데이터포털 API 컨트롤러
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 6.  9.    MICHAEL						최초작성
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/data")
public class DataApiController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private URL obj = null;
    private HttpURLConnection con = null;
    private BufferedReader in = null;
    private StringBuffer response = null;
    private DataOutputStream out = null;
    private int responseCode;

    /**
     * 공공데이터활용지원센터_보건복지부 코로나19 시·도발생 현황
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 공공데이터활용지원센터_보건복지부 코로나19 시·도발생 현황
    @RequestMapping(value = "/corona/status", produces="application/xml;charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> status(HttpServletRequest req, HttpServletResponse res ) {
        // https://www.data.go.kr/data/15043378/openapi.do
        try{

            // SSL 인증서 오류 방지
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new TrustManager() }, null);
            SSLContext.setDefault(ctx);

            StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?"); /*URL*/

            // 파라미터 셋팅
            Map<String, String> body = new HashMap<>();
            body.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            body.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            body.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            body.put(URLEncoder.encode("startCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8")); /*검색할 생성일 범위의 시작*/
            body.put(URLEncoder.encode("endCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8"));/*검색할 생성일 범위의 종료*/
            String parameters =  mapToString(body);
            // 파라미터 셋팅

            String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?"+parameters;

            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총
            responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("조회결과 : " + response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null){
                try{
                    in.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }

            if(con != null){
                con.disconnect();
            }
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }


    // POST 방식
    @RequestMapping(value = "/corona/status2", produces="application/xml;charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> status2(HttpServletRequest req, HttpServletResponse res ) {

        try{

            // SSL 인증서 오류 방지
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new TrustManager() }, null);
            SSLContext.setDefault(ctx);

            // 파라미터 셋팅
            Map<String, String> body = new HashMap<>();
            body.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            body.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            body.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            body.put(URLEncoder.encode("startCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8")); /*검색할 생성일 범위의 시작*/
            body.put(URLEncoder.encode("endCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8"));/*검색할 생성일 범위의 종료*/
            String parameters =  mapToString(body);
            // 파라미터 셋팅

            String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?"+parameters;

            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            //Request Header 정의 - 필요시 사용
            //con.setRequestProperty("x-user-ci", "uKM41JqzBg8kUlKf4YM7jsqdhagQURkE");
            con.setRequestProperty("Content-type", "application/json");
            out = new DataOutputStream(con.getOutputStream());
            out.write(parameters.getBytes("utf-8"));
            out.flush();

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총

            responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("조회결과 : " + response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null){
                try{
                    in.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }

            if(out != null){
                try{
                    out.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }

            if(con != null){
                con.disconnect();
            }
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }


    /**
     * 기상청_동네예보 조회서비스
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 기상청_동네예보 조회서비스
    @RequestMapping(value = "/city/weather", produces="application/xml;charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> weather(HttpServletRequest req, HttpServletResponse res ) {
        // https://www.data.go.kr/data/15057682/openapi.do
        // X , Y 좌표는 위 링크가서 첨부파일(엑셀) 활용
        try{

            // SSL 인증서 오류 방지
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new TrustManager() }, null);
            SSLContext.setDefault(ctx);

            // 파라미터 셋팅
            Map<String, String> body = new HashMap<>();
            body.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            body.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            body.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            body.put(URLEncoder.encode("dataType","UTF-8") , URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            body.put(URLEncoder.encode("base_date","UTF-8") , URLEncoder.encode("20210609", "UTF-8"));  /*21년 6월 9일 발표*/
            body.put(URLEncoder.encode("base_time","UTF-8") , URLEncoder.encode("1400", "UTF-8")); /*14시 발표(정시단위)*/
            body.put(URLEncoder.encode("nx","UTF-8") , URLEncoder.encode("63", "UTF-8"));/*예보지점의 X 좌표값*/
            body.put(URLEncoder.encode("ny","UTF-8") , URLEncoder.encode("125", "UTF-8")); /*예보지점 Y 좌표*/
            String parameters =  mapToString(body);
            // 파라미터 셋팅

            String url ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?"+parameters;

            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총
            responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("조회결과 : " + response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null){
                try{
                    in.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }

            if(con != null){
                con.disconnect();
            }
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }


    /**
     * 한국환경공단_에어코리아_미세먼지 경보 발령 현황
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 한국환경공단_에어코리아_미세먼지 경보 발령 현황
    @RequestMapping(value = "/city/fineDust", produces="application/xml;charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> fineDust(HttpServletRequest req, HttpServletResponse res ) {
        // https://www.data.go.kr/data/15073885/openapi.do
        // 첨부파일(엑셀) 활용
        try{

            // SSL 인증서 오류 방지
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new TrustManager() }, null);
            SSLContext.setDefault(ctx);

            // 파라미터 셋팅
            Map<String, String> body = new HashMap<>();
            body.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            body.put(URLEncoder.encode("returnType","UTF-8") , URLEncoder.encode("xml", "UTF-8"));  /*xml 또는 json*/
            body.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            body.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            body.put(URLEncoder.encode("year","UTF-8") , URLEncoder.encode("2021", "UTF-8")); /*측정 연도*/
            body.put(URLEncoder.encode("itemCode","UTF-8") , URLEncoder.encode("PM10", "UTF-8"));  /*미세먼지 항목 구분(PM10, PM25), PM10/PM25 모두 조회할*/
            String parameters =  mapToString(body);
            // 파라미터 셋팅

            String url ="http://apis.data.go.kr/B552584/UlfptcaAlarmInqireSvc/getUlfptcaAlarmInfo?"+parameters;

            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총
            responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("조회결과 : " + response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null){
                try{
                    in.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }

            if(con != null){
                con.disconnect();
            }
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }


    /**
     * Method mapToString.
     * @return String
     */
    @SuppressWarnings("rawtypes")
    public static String mapToString(Map<String,String> obj){

        Set keySet = obj.keySet();
        Iterator keys = keySet.iterator();
        String parameters = "";
        while (keys.hasNext()) {
            String paramKey = (String) keys.next();
            String paramValue = (String) obj.get(paramKey);
            if (parameters.equals("")) {
                parameters += paramKey + "=" + paramValue;
            } else {
                parameters += "&" + paramKey + "=" + paramValue;
            }
        }

        return parameters;
    }

    // SSL 인증서 관련 메서드 시작
    private static class HostVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String paramString, SSLSession paramSSLSession) {
            return true;
        }
    }

    private static class TrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                throws CertificateException {
        }
    }
    // SSL 인증서 관련 메서드 종료

}
