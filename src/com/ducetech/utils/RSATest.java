package com.ducetech.utils;

import java.util.Map;


public class RSATest {
	private static String publicKey;
	private static String privateKey;

	static {
		try {
			Map<String, Object> keyMap = RSAUtil.genKeyPair();
			publicKey = RSAUtil.getPublicKey(keyMap);
			privateKey = RSAUtil.getPrivateKey(keyMap);
			System.err.println("公钥: \n\r" + publicKey);
			System.err.println("私钥： \n\r" + privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		test();
		testSign();
	}

	public static void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String source = "{\"saleid\": \"10001000\",\"saleman\": \"营业员\",\"syman\": \"制单人\",\"customer\": \"客户信息\",\"storename\": \"库房\",\"creatdate\": \"2014-05-11 11:11:11\",\"batchdate\": \"2014-05-11 11:15:11\",\"productArr\": [{\"itemnumber\": \"商品编码_1.1\",\"quantity\": \"2\",\"saleprice\": \"500\",\"amount\": \"1000\"},{\"itemnumber\": \"商品编码_1.2\",\"quantity\": \"2\", \"saleprice\": \"500\", \"amount\": \"1000\"}],\"paymentArr\": [ \"支付信息一：2000\",\"支付信息二：3000\",\"支付信息三：4000\"]}";
		System.out.println("\r加密前文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtil.encryptByPublicKey(data, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtil.decryptByPrivateKey(encodedData, privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	public static void testSign() throws Exception {
		System.err.println(" \r\n私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtil.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtil.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.err.println("私钥签名——公钥验证签名");
		String sign = RSAUtil.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);
		boolean status = RSAUtil.verify(encodedData, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}
}
