package com.ducetech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SHAUtil {
	 public static void main(String[] args) throws Exception {  
	        encodeByMAC("中国oP……&*（）…&802134…");  
	        encodeBySHA("中国oP……&*（）…&802134…");  
	        shaFile();  
	    }  
	    /** 
	     * 使用MAC 算法的 消息摘要 
	     * @param data 
	     * @throws Exception 
	     */  
	    public static void encodeByMAC(String data) throws Exception{  
//	      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");  
//	      SecretKey key = keyGen.generateKey(); //这个每次生成的key不一样, 此处不能使用  
	          
	        PBEKeySpec keySpec = new PBEKeySpec("randomkey^(^&*^%$".toCharArray());  
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");  
	        SecretKey key = keyFactory.generateSecret(keySpec);  
	          
	        /* 
	         *  此类提供“消息验证码”（Message Authentication Code，MAC）算法的功能。 
	         *  MAC 基于秘密密钥提供一种方式来检查在不可靠介质上进行传输或存储的信息的完整性。 
	         *  通常，消息验证码在共享秘密密钥的两个参与者之间使用，以验证这两者之间传输的信息。 
	         *  基于加密哈希函数的 MAC 机制也叫作 HMAC。结合秘密共享密钥， 
	         *  HMAC 可以用于任何加密哈希函数（如 MD5 或 SHA-1） 
	         */  
	        Mac mac = Mac.getInstance("HmacSHA1");  
	        //以下三种都可用  
//	      Mac mac = Mac.getInstance("HmacSHA256");  
//	      Mac mac = Mac.getInstance("HmacSHA384");  
//	      Mac mac = Mac.getInstance("HmacSHA512");  
	        mac.init(key);  
	        byte[] dest = mac.doFinal(data.getBytes());  
	        System.out.println(dest.length);  
	        System.out.println("MAC摘要：" + Arrays.toString(dest));  
	    }  
	      
	    /** 
	     * SHA1加密  使用消息摘要MessageDigest 处理 
	     * @throws Exception  
	     */  
	    public static String encodeBySHA(String str) throws Exception{  
	        MessageDigest sha1;  
	        sha1 = MessageDigest.getInstance("SHA1");  
	        //以下三种不可用  
//	      sha1 = MessageDigest.getInstance("SHA256");  
//	      sha1 = MessageDigest.getInstance("SHA384");  
//	      sha1 = MessageDigest.getInstance("SHA512");  
	          
	        sha1.update(str.getBytes()); //先更新摘要  
	        byte[] digest = sha1.digest(); //再通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。  
	          
	        /* 
	         * 使用指定的 byte 数组对摘要进行最后更新，然后完成摘要计算。 
	         * 也就是说，此方法首先调用 update(input)， 
	         * 向 update 方法传递 input 数组，然后调用 digest()。 
	         */  
//	      byte[] digest = sha1.digest(str.getBytes());  
	        String hex = Hex.encodeHexString(digest);  
	        System.out.println("SHA1摘要:" + hex);  
	        return hex;  
	    }  
	      
	    /** 
	     * 文件数据摘要 
	     * @throws Exception 
	     */  
	    public static void shaFile() throws Exception {  
	        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");  
	        DigestOutputStream dos = new DigestOutputStream(new FileOutputStream(new File("/home/lenzhao/data/abc.txt")), messageDigest);  
	        dos.write("中华人民……&（））f*（214）admin*".getBytes());  
	        dos.close();  
	        byte[] digest = messageDigest.digest();  
	        System.out.println("使用流写文件，该文件的摘要为:" + Hex.encodeHexString(digest));  
	          
	        DigestInputStream dis = new DigestInputStream(new FileInputStream(new File("/home/lenzhao/data/abc.txt")), messageDigest);  
	        byte[] buf = new byte[100];  
	        int len;  
	        while ((len = dis.read(buf)) != -1) {  
	            System.out.println("读取到的数据为：" + new String(buf, 0, len));  
	        }  
	        dis.close();  
	        byte[] digest2 = messageDigest.digest();  
	        //当流读取完毕，即将文件读完了， 这时的摘要 才与 写入时的 一样  
	        System.out.println("使用流读文件，该文件的摘要为:" + toHex(digest2));  
	    }  
	      
	    
	    /** 
	     *字符数组转16进制(自己实现)
	     * @param digest 
	     * @return 
	     */  
	    private static String toHex(byte[] digest) {  
	        StringBuilder sb = new StringBuilder();  
	        int len = digest.length;  
	        String out = null;  
	        for (int i = 0; i < len; i++) {  
//	          out = Integer.toHexString(0xFF & digest[i] + 0xABCDEF); //加任意 salt  
	            out = Integer.toHexString(0xFF & digest[i]);//原始方法  
	            if (out.length() == 1) {  
	                sb.append("0");//如果为1位 前面补个0  
	            }  
	            sb.append(out);  
	        }  
	        return sb.toString();  
	    }  
}
