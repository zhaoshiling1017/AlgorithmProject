package com.ducetech.auth;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 *配置属性
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2016-11-23 下午1:39:31
 */
public class Configs extends Properties{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5768247833229017526L;
	
	private static Configs instance= null;
	
	private static final String  propFileName = "/configs.properties";
	
	
	private Configs() {
	}
	/**
	 * constructor
	 * loading the properties files to inputStream
	 */
	private Configs(String propFileName){
		
		InputStream inputStream = getClass().getResourceAsStream(propFileName); 
		try {
			load(inputStream);
		} catch (IOException e) {
			System.out.println("加载属性文件到输入流错误");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * getInstance (belong the confirm of no instance already exists)
	 */
	public static Configs getInstance(){
		if(instance != null){
			return instance;
		}
		else{
			makeInstance(propFileName);
			return instance;
		}
		
	}
	
	/**
	 * mkeInstance on condition of no instance exist
	 */
	private static synchronized void makeInstance(String propFileName){
		if(instance == null){
			instance = new Configs(propFileName);
		}
	}
	
	public static void main(String[] args) {
		Configs dbp = Configs.getInstance();
		String str = String.valueOf(dbp.get("auth.ip.allowed"));
		System.out.println(Arrays.toString(str.split(",")));
	}
	
	public String[] getAllowedList() {
		return String.valueOf(super.get("auth.ip.allowed")).split(",");
	}
	public String[] getDeniedList() {
		return String.valueOf(super.get("auth.ip.denyed")).split(",");
	}
	
}
