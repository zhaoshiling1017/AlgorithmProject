package com.unicss;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {
	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String encodeByMd5(String str){
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(str.getBytes());
			result = Hex.encodeHexString(bytes).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
