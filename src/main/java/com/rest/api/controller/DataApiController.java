package com.rest.api.controller;

import com.rest.api.util.HttpURLUtil;
import com.rest.api.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
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
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 *
 *
 *
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

        String result = "";
        try{

            // 파라미터 셋팅
            Map<String, String> param = new HashMap<>();
            param.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            param.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            param.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            param.put(URLEncoder.encode("startCreateDt","UTF-8") , URLEncoder.encode("20210706", "UTF-8")); /*검색할 생성일 범위의 시작*/
            param.put(URLEncoder.encode("endCreateDt","UTF-8") , URLEncoder.encode("20210706", "UTF-8"));/*검색할 생성일 범위의 종료*/
            // 파라미터 셋팅
            String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
            result = HttpURLUtil.getJson(req,res,url,param);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }
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

        String result = "";
        try{

            // 파라미터 셋팅
            Map<String, String> param = new HashMap<>();
            param.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            param.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            param.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            param.put(URLEncoder.encode("dataType","UTF-8") , URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            param.put(URLEncoder.encode("base_date","UTF-8") , URLEncoder.encode("20210706", "UTF-8"));  /*21년 6월 9일 발표*/
            param.put(URLEncoder.encode("base_time","UTF-8") , URLEncoder.encode("1400", "UTF-8")); /*14시 발표(정시단위)*/
            param.put(URLEncoder.encode("nx","UTF-8") , URLEncoder.encode("63", "UTF-8"));/*예보지점의 X 좌표값*/
            param.put(URLEncoder.encode("ny","UTF-8") , URLEncoder.encode("125", "UTF-8")); /*예보지점 Y 좌표*/
            // 파라미터 셋팅

            String url ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
            result = HttpURLUtil.getJson(req,res,url,param);

            System.out.println("조회결과 : " + result);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }
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

        String result = "";

        try{

            // 파라미터 셋팅
            Map<String, String> param = new HashMap<>();
            param.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            param.put(URLEncoder.encode("returnType","UTF-8") , URLEncoder.encode("xml", "UTF-8"));  /*xml 또는 json*/
            param.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            param.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            param.put(URLEncoder.encode("year","UTF-8") , URLEncoder.encode("2021", "UTF-8")); /*측정 연도*/
            param.put(URLEncoder.encode("itemCode","UTF-8") , URLEncoder.encode("PM10", "UTF-8"));  /*미세먼지 항목 구분(PM10, PM25), PM10/PM25 모두 조회할*/
            // 파라미터 셋팅
            String url ="http://apis.data.go.kr/B552584/UlfptcaAlarmInqireSvc/getUlfptcaAlarmInfo";
            result = HttpURLUtil.getJson(req,res,url,param);

            System.out.println("조회결과 : " + result);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/corona/test", produces="application/xml;charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> test(HttpServletRequest req, HttpServletResponse res ) {
        // https://www.data.go.kr/data/15043378/openapi.do
        ResponseEntity<String> re = null;
        try{

            // 파라미터 셋팅
            Map<String, String> param = new HashMap<>();
            param.put(URLEncoder.encode("ServiceKey","UTF-8") , "EA%2BL0pXtzc04Vf6fi9AaKqWiOpG6kssrT6D9ajZh0ZTaHbGxK2uBFs4Usink8kQyukngeP2lp69tU4v14HC5QA%3D%3D"); /*Service Key*/
            param.put(URLEncoder.encode("pageNo","UTF-8") , URLEncoder.encode("1", "UTF-8"));  /*페이지번호*/
            param.put(URLEncoder.encode("numOfRows","UTF-8") , URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            param.put(URLEncoder.encode("startCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8")); /*검색할 생성일 범위의 시작*/
            param.put(URLEncoder.encode("endCreateDt","UTF-8") , URLEncoder.encode("20210609", "UTF-8"));/*검색할 생성일 범위의 종료*/
            // 파라미터 셋팅
            String url ="http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

            re = RestTemplateUtil.getJson(req,res,url,param,entity);

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            return new ResponseEntity<String>(re.getBody(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/corona/test2")
    @ResponseBody
    public ResponseEntity<String> test2(HttpServletRequest req, HttpServletResponse res ) {
        // https://www.data.go.kr/data/15043378/openapi.do
        ResponseEntity<String> re = null;
        try{

            String url ="https://developers.mydatakorea.org:9443/v1/bank/accounts/invest/basic";
            Map<String, String> param = new HashMap<>();
            param.put(URLEncoder.encode("org_code","UTF-8") , "SHTB010101");
            param.put(URLEncoder.encode("account_num","UTF-8") , URLEncoder.encode("1", "UTF-8"));
            param.put(URLEncoder.encode("seqno","UTF-8") , URLEncoder.encode("10", "UTF-8"));
            param.put(URLEncoder.encode("search_timestamp","UTF-8") , URLEncoder.encode("20210629100800", "UTF-8"));

            String header = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxODkiLCJpc3MiOiJ0ZXN0IGJlZCIsImF1ZCI6IjgyIiwianRpIjoiMDAwMDAxNzktZDYwYS1jNGUyLTAwMDAtMDE3OWJjMjhiM2JiIiwiZXhwIjoxNjMwNTY5NzAwLCJzY29wZSI6ImNhIGJhbmsubGlzdCBiYW5rLmludmVzdCBiYW5rLmxvYW4gYmFuay5pcnAgY2FyZC5saXN0IGNhcmQuY2FyZCBjYXJkLnBvaW50IGNhcmQuYmlsbCBjYXJkLmxvYW4gaW52ZXN0Lmxpc3QgaW52ZXN0LmFjY291bnQgaW52ZXN0LmlycCBiYW5rLmRlcG9zaXQgZWZpbi5wcmVwYWlkIGVmaW4ucGFpZCBpbnN1Lmxpc3QgaW5zdS5pbnN1cmFuY2UgaW5zdS5sb2FuIGluc3UuaXJwIGVmaW4ubGlzdCBjYXBpdGFsLmxpc3QgY2FwaXRhbC5sb2FuIGdpbnN1Lmxpc3QgZ2luc3UuaW5zdXJhbmNlIHRlbGVjb20ubGlzdCB0ZWxlY29tLm1nbXQgbWFuYWdlIn0.D60Zlc8fyq6fHVSQZBrgyKibthbhz08qrkULmljOsqPUmGurT6MWBe-d24-f13TVN_i1R_ew9nHAeI1tFo6bG_LW5kRbIqD83UWxnIrMfzWcLxHRBKu_rDSFnxmlHxQem3C3tiEpsFVIrzrFCQQswD_3pwz0OWZEU6dDSkHsFxOJ-xKPvcWqmXpsv2b3qv78cMWHETiWwd8KRKYvvobY7chpjkkm22BGeZ5FWAKqypsOuTAJhTQRyKSLM6QKjG_Na1e5FvmEFnR0e0dwFnU9qu-j5wAHjR7qakbMDWdBvnLdCiHxulW0EZXEbV7XrK-0YWolB_k0TNO7p87upQaWRg";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", header);
            headers.add("x-user-ci", "uKM41JqzBg8kUlKf4YM7jsqdhagQURkE");
            headers.add("x-api-tran-id", "4048801311MA20210603");
            headers.add("X-FSI-SVC-DATA-KEY", "Y");
            headers.add("X-FSI-UTCT-TYPE", "TGC00001");
            HttpEntity entity = new HttpEntity(param, headers);

            re = RestTemplateUtil.postJson(req,res,url,param,entity);

            System.out.println("조회결과 : " + re.toString());

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
        return new ResponseEntity<String>(re.toString(), HttpStatus.OK);
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

    public static class TrustManager implements X509TrustManager {

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
