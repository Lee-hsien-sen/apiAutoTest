package com.qingzi.testUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
	public static void main(String[] args) {
		String s=ReadProperties.GetTestPropertyByKey("xls");
		System.out.println(s);
//		ClearProperty();
//		SetProperty("123","111");
	}
	
    public static String GetPropertyByKey(String key) {
        Properties pps = new Properties();
        try {
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"class.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(sourceFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            return null;
        }
    }
    
    public static String GetTestPropertyByKey(String key) {
        Properties pps = new Properties();
        try {
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"test.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(sourceFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            return null;
        }
    }
    
    //得到information中的值
    public static String GetinformationByKey(String key) {
        Properties pps = new Properties();
        try {
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"information.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(sourceFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            return null;
        }
    }
    
    //
    public static void updateProperty(String name,String value){
    	Properties prop = new Properties();// 属性集合对象
    	File directory = new File(".");
        try {
			String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"information.properties";
			FileInputStream fis = new FileInputStream(sourceFile);// 属性文件输入流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流
			
			// 获取属性值，sitename已在文件中定义
//			System.out.println("获取属性值：password=" + prop.getProperty("password"));
			// 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
			// System.out.println("获取属性值：country=" + prop.getProperty("country", "中国"));
			
			// 修改sitename的属性值
			prop.setProperty(name, value);
			// 文件输出流
			FileOutputStream fos = new FileOutputStream(sourceFile);
			// 将Properties集合保存到流中
			prop.store(fos,"");
			fos.close();// 关闭流
//			Log.logInfo("成功保存环境信息文件。");
		} catch (IOException e) {
//			Log.logError("保存环境信息文件失败。");
			e.printStackTrace();
		}
    }
    //判断Properties文件是否为空
    public static boolean isBoolean(){
    	File directory = new File(".");
    	try {
			String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"information.properties";
			Properties prop = new Properties();// 属性集合对象
			FileInputStream fis = new FileInputStream(sourceFile);// 属性文件输入流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			boolean b=prop.isEmpty();
			fis.close();// 关闭流
			return b;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
	}
    
    public static void ClearProperty() {
    	File directory = new File(".");
        try {
        	String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"information.properties";
        	File file =new File(sourceFile);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    public static String GetPropertyByClassName(String key) {
        Properties pps = new Properties();
        try {
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"reflect.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(sourceFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
            
        }catch (IOException e) {
            return null;
        }
    }  
    public static String GetPropertyByClassName(String key,String qudao) {
        Properties pps = new Properties();
        try {
        	
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"reflect.properties";
            InputStream in = new BufferedInputStream (new FileInputStream(sourceFile));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
            
        }catch (IOException e) {
        	
            return null;
        }
    }  
}
