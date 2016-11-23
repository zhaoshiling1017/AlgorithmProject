package com.ducetech.auth;



import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 加密认证入口类
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2016-11-23 下午1:43:49
 */
public class AuthClient {

	public static void main(String[] args) throws Exception {
		
	}

	private static final String UTF_8 = "UTF-8";
	private static  String publicKey = null;

	static{
		try {
			Object publicPath = Configs.getInstance().get("publickey");
			if (null != publicPath) {
				publicKey = FileUtil.readFile(String.valueOf(publicPath));
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

	
	public static String decryptRD(byte[] data, byte[] sign) throws Exception {
		Gson gson = new Gson();
		String signDec = decryptRSA(sign);
		Map<String,String> signMap = gson.fromJson(signDec,  new TypeToken<Map<String, String>>() {}.getType());
		String key = signMap.get("key");
		return decrypt(data, key);
	}
	
	/**
	 * RSA+DES解密
	 * @param data
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static String decryptRD(String data, String sign) throws Exception {
		return decryptRD(data.getBytes(UTF_8), sign.getBytes(UTF_8));
	}

	/**
	 * RSA公钥加密
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptRSA(byte[] bytes) throws Exception {
		byte[] b = RSACoder.encryptByPublicKey(bytes, publicKey);
		return b;
	}
	
	/**
	 * RSA 公钥解密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private static String decryptRSA(byte[] str) throws Exception {
		return new String(RSACoder.decryptByPrivateKey(str, publicKey));
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