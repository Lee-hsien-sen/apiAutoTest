package com.qingzi.testUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadHtml {

	public static void main(String[] args) {
		try {
			readFile_("overview.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String readFile_(String htmlfilename)throws IOException{
		
		File directory = new File(".");
        String sourceFile = directory.getCanonicalPath() +File.separator+"test-output"+File.separator+"html"+File.separator+htmlfilename;
        
		FileInputStream fis = new FileInputStream(sourceFile);

		byte[] buf = new byte[1024];
		int len = 0;
		StringBuffer sb=new StringBuffer();
		while((len=fis.read(buf))!=-1)
		{
			sb.append(new String(buf,0,len));
//			System.out.println(new String(buf,0,len));
		}

		fis.close();
		
//		System.out.println(sb);
		return sb.toString();
		
	}

}
