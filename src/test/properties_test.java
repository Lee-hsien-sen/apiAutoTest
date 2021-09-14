package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class properties_test {

	public static void main(String[] args) throws Exception{
		test3();
//		 ClearProperty(); 
		

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
	public static void test3() throws Exception{
		Properties prop = new Properties();// 属性集合对象
		FileInputStream fis = new FileInputStream("src/resources/information.properties");// 属性文件输入流
		prop.load(fis);// 将属性文件流装载到Properties对象中
		System.out.println(prop.isEmpty());
		fis.close();// 关闭流
	}
	
	
	public static void test2() throws Exception {
		Properties prop = new Properties();// 属性集合对象
		FileInputStream fis = new FileInputStream("src/resources/information.properties");// 属性文件输入流
		prop.load(fis);// 将属性文件流装载到Properties对象中
		fis.close();// 关闭流

		// 获取属性值，sitename已在文件中定义
		System.out.println("获取属性值：password=" + prop.getProperty("password"));
		// 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
		// System.out.println("获取属性值：country=" + prop.getProperty("country", "中国"));

		// 修改sitename的属性值
		prop.setProperty("password", "heihei");
		// 文件输出流
		FileOutputStream fos = new FileOutputStream("src/resources/information.properties");
		// 将Properties集合保存到流中
		prop.store(fos, "Copyright (c) Boxcode Studio");
		fos.close();// 关闭流
		System.out.println("获取修改后的属性值：password=" + prop.getProperty("password"));
	}
	
	public static String test1(){
		Properties prop = new Properties(); 
        try {
        	File directory = new File(".");
            String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"xyzb.properties";
            FileOutputStream oFile = new FileOutputStream(sourceFile, true);//true表示追加打开
            prop.setProperty("phone", "10086");
            prop.store(oFile, "The New properties file");
            oFile.close();

            return "";
            
        }catch (IOException e) {
            return null;
        }
	}

}
