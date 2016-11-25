package com.ducetech.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.ducetech.auth.AuthClient;
import com.ducetech.auth.Coder;
import com.ducetech.auth.DESCoder;
import com.google.gson.JsonObject;

public class HttpUtil {
	public static final int CONNECTION_TIMEOUT = 5000;
	//httpclient post请求
	public static String httpPost(Map<String, String> paramMap, String urlStr ) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			//连接时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
			//数据传送时间
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpPost httpPost = new HttpPost(urlStr);
			TreeMap<String, String> treeMap = new TreeMap<String, String>();
			if (paramMap != null) {
				treeMap.putAll(paramMap);
			}
			Iterator<String> iterator = treeMap.keySet().iterator();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			  while (iterator.hasNext()) {
                  String key = iterator.next();
                  params.add( new BasicNameValuePair(key, treeMap.get(key)));
            }
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
			httpPost.setEntity(uefEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			InputStream inputStream = httpEntity.getContent();
			//获取返回的数据信息
			StringBuffer postResult = new StringBuffer();
			String readLine = null ;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((readLine = reader.readLine()) != null) {
				postResult.append(readLine);
			}
			httpClient.getConnectionManager().shutdown();
			return postResult.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//http post 请求, 传送json格式数据
	public static String httpPostForJSON(Map<String, String> paramMap, String urlStr ){
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlStr);
			TreeMap<String, String> treeMap = new TreeMap<String, String>();
			if (paramMap != null) {
				treeMap.putAll(paramMap);
			}
			Iterator<String> iterator = treeMap.keySet().iterator();
			//List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = new JSONObject();
			while (iterator.hasNext()) {
				String key = iterator.next();
				json.put(key, treeMap.get(key));
				//params.add(new BasicNameValuePair(key, treeMap.get(key)));
			}
			 StringEntity s = new StringEntity(json.toString());  
	         s.setContentEncoding("UTF-8"); 
	         s.setContentType("application/json"); 
	         httpPost.setEntity(s);
			//发送json数据
			/* JSONObject json = new JSONObject();
	         Object email = "zhaoshiling1017@163.com";
	         json.put("email", email);
	         Object pwd = "123456";
	         json.put("password", pwd);
	         StringEntity s = new StringEntity(json.toString());  
	         s.setContentEncoding("UTF-8"); 
	         s.setContentType("application/json"); 
	         httpPost.setEntity(s);*/
			/*UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
			httpPost.setEntity(uefEntity);*/
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			InputStream inputStream = httpEntity.getContent();
			//获取返回的数据信息
			StringBuffer postResult = new StringBuffer();
			String readLine = null ;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((readLine = reader.readLine()) != null) {
				postResult.append(readLine);
			}
			httpClient.getConnectionManager().shutdown();
			return postResult.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//httpclient get 请求
	 public static String httpGet( Map<String, String> paramsMap,String url){
		 try{
			 HttpClient httpClient = new DefaultHttpClient();
		    	String rsp = null;
		    	StringBuffer sb = new StringBuffer(url);
		    	if(paramsMap != null && !paramsMap.isEmpty())
		    	{
		    		for(Entry<String, String> entry:paramsMap.entrySet())
		    			sb.append(sb.indexOf("?")<0?"?":"&").append(entry.getKey()).append("=").append(entry.getValue());
		    	}
		    	HttpGet httpGet = new HttpGet(sb.toString());
		    	httpGet.getParams().setParameter("http.socket.timeout", new Integer(CONNECTION_TIMEOUT));
		    	HttpResponse response = httpClient.execute(httpGet);
		    	rsp = EntityUtils.toString(response.getEntity(), "UTF-8");
		    	return rsp;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return null;
	}
	public static void main(String[] args) throws Exception {
		JsonObject json = new JsonObject();
		json.addProperty("userName", "18612700346");
		json.addProperty("password", "ducetech2016");
		//json.addProperty("accessToken", "e91004b6-ddf9-496d-8cc9-37722b759541");
		String key = DESCoder.initKey();
		byte[] sign = AuthClient.produceSign(key);
		byte[] data = AuthClient.encrypt(json.toString().getBytes("UTF-8"), key);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("data", URLEncoder.encode(Coder.encryptBASE64(data), "UTF-8"));
		//paramMap.put("sign", URLEncoder.encode(Coder.encryptBASE64(sign), "UTF-8"));
		paramMap.put("data", Coder.encryptBASE64(data));
		paramMap.put("sign", Coder.encryptBASE64(sign));
		String jsonStr = httpPostForJSON(paramMap, "http://127.0.0.1:9005/interface/login");
		JSONObject obj = JSONObject.fromObject(jsonStr);
		String data2 = obj.getString("data");
		String sign2 = obj.getString("sign");
		String result = AuthClient.decryptRD(data2, sign2);
		System.out.println(result);
		/*paramMap.put("sendno", "4294967");
		paramMap.put("app_key", "f9393973ecc12ab885be62d1");
		paramMap.put("receiver_type", "5");
		paramMap.put("receiver_value", "020e613328d");
		paramMap.put("msg_type", "2");
		paramMap.put("msg_content", "{\"message\":\"自定义消息\"}");
		paramMap.put("verification_code", "7f7cad1fdc1f1a4b16c0b13cc7184e29".toUpperCase());
		System.out.println(httpPost(paramMap, "http://api.jpush.cn:8800/v2/push"));*/
	}
}
