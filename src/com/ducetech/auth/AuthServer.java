package com.ducetech.auth;



import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 加密认证入口类
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2016-11-23 下午1:43:49
 */
public class AuthServer {

	public static void main(String[] args) throws Exception {
		JsonObject json = new JsonObject();
		json.addProperty("userName", "18612700346");
		json.addProperty("password", "123456");
		String key = DESCoder.initKey();
		byte[] sign = produceSign(key);
		byte[] data = encrypt(json.toString().getBytes(UTF_8), key);
		String jsondata = AuthClient.decryptRD(Coder.encryptBASE64(data), Coder.encryptBASE64(sign));
		System.out.println(jsondata);
	}

	private static final String UTF_8 = "UTF-8";
	private static  String privateKey = null;

	static{
		try {
			Object privatePath = Configs.getInstance().get("privatekey");
			if (null != privatePath) {
				privateKey = FileUtil.readFile(String.valueOf(privatePath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成签名
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] produceSign(String key) throws Exception {
		String s = "{\"time\":\""+new Date().getTime()+"\",\"key\":\""+key+"\"}";
		return encryptRSA((s).getBytes(UTF_8));
	}
	
	
	/**
	 * DES 加密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] str,String key) throws Exception {
		return DESCoder.encrypt(str, key);
	}

	/**
	 * RSA+DES解密
	 * @param data
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static String decryptRD(byte[] data, byte[] sign) throws Exception {
		Gson gson = new Gson();
		String signDec = decryptRSA(sign);
		Map<String,String> signMap = gson.fromJson(signDec,  new TypeToken<Map<String, String>>() {}.getType());
		String key = signMap.get("key");
		return decrypt(data, key);
	}
	
	
	public static String decryptRD(String data, String sign) throws Exception {
		return decryptRD(Coder.decryptBASE64(data), Coder.decryptBASE64(sign));
	}

	/**
	 * RSA私钥加密
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptRSA(byte[] bytes) throws Exception {
		byte[] b = RSACoder.encryptByPrivateKey(bytes, privateKey);
		return b;
	}
	
	/**
	 * RSA 私钥解密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private static String decryptRSA(byte[] str) throws Exception {
		return new String(RSACoder.decryptByPrivateKey(str, privateKey));
	}
	
	/**
	 * DES 解密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String decrypt(byte[] str,String key) throws Exception {
		return new String(DESCoder.decrypt(str, key),Charset.forName(UTF_8));
	}

}