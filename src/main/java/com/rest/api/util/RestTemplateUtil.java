package com.rest.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RestTemplateUtil {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static ResponseEntity<String> getJson(HttpServletRequest req, HttpServletResponse res, String url, Map<String, String> parma , HttpEntity entity){

        ResponseEntity<String> re = null;

        try {
            ssl();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));// UTF-8 설정
            URI apiUrl = URI.create(url+"?"+mapToString(parma));
            re = restTemplate.exchange(apiUrl , HttpMethod.GET , entity , String.class );
            System.out.println("조회결과 : " + re.toString());
        }catch (Exception o_O){
            o_O.printStackTrace();
        }finally {

            return re;
        }

    }

    public static ResponseEntity<String> postJson(HttpServletRequest req, HttpServletResponse res, String url, Map<String, String> parma , HttpEntity entity){

        ResponseEntity<String> re = null;

        try {
            ssl();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));// UTF-8 설정
            re = restTemplate.exchange(url ,HttpMethod.POST , entity , String.class );
            System.out.println("조회결과 : " + re.toString());
        }catch (Exception o_O){
            o_O.printStackTrace();
        }finally {

            return re;
        }

    }

    public static void ssl() throws NoSuchAlgorithmException, KeyManagementException {
        // SSL 인증서 오류 방지
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new HttpURLUtil.TrustManager[] { new HttpURLUtil.TrustManager() }, null);
        SSLContext.setDefault(ctx);
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
