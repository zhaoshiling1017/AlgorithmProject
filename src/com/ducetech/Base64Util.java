package com.ducetech;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;
	/**
	 * Base64加密
	 * @param str
	 * @return
	 */
	public static String encodeByBase64(String str){
		String result = null;
		try {
			result = Base64.encodeBase64String(str.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Base64解密
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String decodeByBase64(String str, String charset){
		String result = null;
		try {
			if(null != charset && !"".equals(charset.trim())){
				result = new String(Base64.decodeBase64((str.getBytes())), charset);
			}else{
				result = new String(Base64.decodeBase64((str.getBytes())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 将文件编码为BASE64字符串
	 * 大文件慎用，可能会导致内存溢出
	 * @param filePath 文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeFile(String filePath) throws Exception {
		byte[] bytes = fileToByte(filePath);
		return Base64.encodeBase64String(bytes);
	}
	/**
	 * BASE64字符串转回文件
	 * @param filePath 文件绝对路径
	 * @param base64  编码字符串
	 * @throws Exception
	 */
	public static void decodeToFile(String filePath, String base64) throws Exception {
		byte[] bytes = Base64.decodeBase64(base64);
		byteArrayToFile(bytes, filePath);
	}

	/**
	 * 文件转换为二进制数组
	 * @param filePath 文件路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(String filePath) throws Exception {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}

	/**
	 * 二进制数据写文件
	 * @param bytes 二进制数据
	 * @param filePath 文件生成目录
	 */
	public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		destFile.createNewFile();
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}
}
