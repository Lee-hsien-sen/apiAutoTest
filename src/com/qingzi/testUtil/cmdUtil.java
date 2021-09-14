package com.qingzi.testUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class cmdUtil {
	
    public static String run(String cmdString) {
        Runtime runtime = Runtime.getRuntime();
        StringBuffer b=null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(cmdString).getInputStream(),"GBK"));
            //StringBuffer b = new StringBuffer();
            String line=null;
            b=new StringBuffer();
            while ((line=br.readLine())!=null) {
                b.append(line+"\n");
            }
//            System.out.println(b.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b.toString();

    }

    public static void main(String[] args) throws IOException {
//    	File directory = new File(".");
//        String sourceFile;
//		sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"case.txt";
    	
//		String command="dir";
//		String command="cd C:\\Program Files (x86)\\PICT && "+"pict "+sourceFile+">"+sourceFile+"caseOut.txt";
//        cmdUtil delp = new cmdUtil();
//        delp.run("dir");
//        System.out.println(delp.run("nslookup www.qq.com").contains("Address:  192.168.10.222"));
//        delp.run("nslookup www.qq.com");
//        delp.run("netsh interface ip set dns \"无线网络连接\" static 192.168.10.222");
        
        try {
//			processBuilderCommand();
        	
        	File directory = new File(".");
            String sourceFile;
            String sourceFile1;
    		sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"case.txt";
    		sourceFile1 = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator;
        	String cmd = "pict "+sourceFile+">"+sourceFile1+"caseOut.txt";
            String cmd2 = "cmd /c  " + "\"\" " + cmd ;
            System.out.println(cmd);
        	run(cmd2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void processBuilderCommand() throws Exception {
        
    	File directory = new File(".");
        String sourceFile;
        String sourceFile1;
		sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"case.txt";
		sourceFile1 = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator;
//        List<String> commands = new ArrayList<>();
//        commands.add("cmd.exe");
//        commands.add("cd C:\\Program Files (x86)\\PICT");
//        commands.add("pict "+sourceFile+">"+sourceFile+"caseOut.txt");
//        commands.add("\r\n");
//        ProcessBuilder pb =new ProcessBuilder(commands);
        //可以修改进程环境变量
//        pb.environment().put("DAXIN_HOME", "/home/daxin");
//        System.out.println(pb.directory());
        String cmd = "pict "+sourceFile+">"+sourceFile1+"caseOut.txt";
        String cmd2 = "cmd /c start " + "\"\" " + cmd ;
        System.out.println(cmd2);

//	    String  []cmd1 = {"cd" , " C:\\Program Files (x86)\\PICT ","pict","case.txt>caseOut.txt"};
        Process process = Runtime.getRuntime().exec(cmd2); 
        process.waitFor();
        process.exitValue();
//        System.out.println(pb.environment());
//         
//        System.out.println(status);
//        InputStream in = process.getInputStream();
//         
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String line = br.readLine();
//        while(line!=null) {
//            System.out.println(line);
//            line = br.readLine();
//        }
//         
    }
}
