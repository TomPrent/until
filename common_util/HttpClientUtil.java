package com.zhejian.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {
	
	public static String postBody(String url,String json){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(url);
		String str=null;
		try{
			StringEntity se=new StringEntity(json,"UTF-8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse httpResponse=httpClient.execute(httpPost);
			//BasicHttpResponse httpResponse=(BasicHttpResponse)httpClient.execute(httpPost);
			HttpEntity resEntity = httpResponse.getEntity();
			str= EntityUtils.toString(resEntity);
		}catch(Exception e){
			System.out.println("请求出错了");
			e.printStackTrace();
		}
		return str;
	}
	
	
	public static String get(String url) throws IOException {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpGet httpgGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = null;
//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectionRequestTimeout(2000).build();
        httpResponse = httpClient.execute(httpgGet);
        InputStream inputStream = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpResponseEntity = httpResponse.getEntity();
            inputStream = httpResponseEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return String.valueOf(stringBuffer);
        }else {
            System.out.println("请求出错!");
        }
        return null;
    }
	
	
	
	/*public static String setSession(CookieStore cookieStore,String url) throws IOException{
		CloseableHttpClient httpClient= null;
		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpgGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = null;
        httpResponse = httpClient.execute(httpgGet);
        InputStream inputStream = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpResponseEntity = httpResponse.getEntity();
            inputStream = httpResponseEntity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return String.valueOf(stringBuffer);
        }else {
            System.out.println("请求出错!");
        }
        return null;
	}*/
	
	
	public static String post(String url, List<NameValuePair> nameValuePairs) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse httpResponse = null;
		HttpEntity httpResponseEntity = null;
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
		httpPost.setEntity(urlEncodedFormEntity);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectionRequestTimeout(30000)
				.build();
		httpPost.setConfig(requestConfig);
		httpResponse = httpClient.execute(httpPost);
		InputStream inputStream = null;
		System.out.println(httpResponse.getStatusLine());
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			httpResponseEntity = httpResponse.getEntity();
			inputStream = httpResponseEntity.getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			System.out.println("----------" + stringBuffer.toString() + "---------");
			return String.valueOf(stringBuffer);
		} else {
			System.out.println("请求出错!");
		}
		if (httpResponse != null)
			httpResponse.close();
		if (httpClient != null)
			httpClient.close();
		if (inputStream != null)
			inputStream.close();
		return null;
	}
	
	
	 /*@SuppressWarnings("resource")
	    public static String httpsPost(String url,String jsonstr,String charset){
	        HttpClient httpClient = null;
	        HttpPost httpPost = null;
	        String result = null;
	        try{
	            httpClient = new SSLClient();
	            httpPost = new HttpPost(url);
	            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
	            StringEntity se = new StringEntity(jsonstr);
	            //se.setContentType("text/json");
	            se.setContentType("application/json");
	            se.setContentEncoding(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
	            httpPost.setEntity(se);
	            HttpResponse response = httpClient.execute(httpPost);
	            if(response != null){
	                HttpEntity resEntity = response.getEntity();
	                if(resEntity != null){
	                    result = EntityUtils.toString(resEntity,charset);
	                }
	            }
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
	        return result;
	    }*/
	 
	 
	 
	 /*public static String httpsGet(String url) throws Exception {
		 	HttpClient httpClient = new SSLClient();
	        HttpGet httpgGet = new HttpGet(url);
	        HttpResponse response =null;
	//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectionRequestTimeout(2000).build();
	        response = httpClient.execute(httpgGet);
	        InputStream inputStream = null;
	        if (response.getStatusLine().getStatusCode() == 200) {
	            HttpEntity httpResponseEntity = response.getEntity();
	            inputStream = httpResponseEntity.getContent();
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            String line = null;
	            StringBuffer stringBuffer = new StringBuffer();
	            while ((line = bufferedReader.readLine()) != null) {
	                stringBuffer.append(line);
	            }
	            return String.valueOf(stringBuffer);
	        }else {
	            System.out.println("请求出错!");
	        }
	        return null;
	    }*/
	 
	 

}
