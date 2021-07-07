package com.rest.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpURLUtil {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    // Request 메소드 정의
    public enum HttpMethod { GET, POST}
    private static URL connUrl = null;
    private static HttpURLConnection con = null;
    private static BufferedReader in = null;
    private static StringBuffer response = null;
    private static DataOutputStream out = null;
    private static int responseCode;

    public static void ssl() throws NoSuchAlgorithmException, KeyManagementException {
        // SSL 인증서 오류 방지
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new TrustManager() }, null);
        SSLContext.setDefault(ctx);
    }

    public static String getJson(HttpServletRequest req, HttpServletResponse res, String url, Map<String, String> parma){

        try {
            ssl();
            connUrl = new URL(url+"?"+mapToString(parma));
            con = (HttpURLConnection) connUrl.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총
            responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

        }catch (Exception o_O){
            o_O.printStackTrace();
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
            return response.toString();
        }

    }

    public static String postJson(HttpServletRequest req, HttpServletResponse res, String url, Map<String, String> parma){

        try {
            ssl();
            connUrl = new URL(url);
            con = (HttpURLConnection) connUrl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-type", "application/json");
            out = new DataOutputStream(con.getOutputStream());
            out.write(mapToString(parma).getBytes("utf-8"));
            out.flush();

            // 필요시 사용
            //con.setConnectTimeout(10000);       //컨텍션타임아웃 10초
            //con.setReadTimeout(5000);           //컨텐츠조회 타임아웃 5총
            responseCode = con.getResponseCode();
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

        }catch (Exception o_O){
            o_O.printStackTrace();
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
            return response.toString();
        }

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
