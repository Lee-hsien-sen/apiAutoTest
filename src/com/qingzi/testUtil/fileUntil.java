package com.qingzi.testUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.qingzi.listener.SkipIInvokedMethodListener;


public class fileUntil {

	public static void main(String[] args) {
		try {
			readFile_("caseOut.txt");
//			WriterFile("12345");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String readFile_(String filename)throws IOException{
		
		File directory = new File(".");
        String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+filename;
		FileInputStream fis = new FileInputStream(sourceFile);

		byte[] buf = new byte[1024];
		int len = 0;
		StringBuffer sb=new StringBuffer();
		while((len=fis.read(buf))!=-1){
			sb.append(new String(buf,0,len));
//			System.out.println(new String(buf,0,len));
		}
		fis.close();
		return sb.toString();
		
	}

	public boolean WriterFile(List<Set<String>> listt){
		
		File directory = new File(".");
        String sourceFile;
		try {
			sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"case.txt";
	        BufferedWriter bw=new BufferedWriter(new FileWriter(sourceFile));
	        String cases="";
	        for(int i=0;i<listt.size();i++){
	        	Object[] obj=listt.get(i).toArray();
	        	cases=Arrays.toString(obj).trim();
	        	cases=cases.substring(1, cases.length()-1);
	        	bw.write(cases);
	        	bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	        return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean WriterFile_map(Integer Percentage){
		
		File directory = new File(".");
        String sourceFile;
		try {
			sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"map.txt";
	        BufferedWriter bw=new BufferedWriter((new OutputStreamWriter(new FileOutputStream(sourceFile),"utf-8")));
	        String cases="";
	        
	        SkipIInvokedMethodListener.serviceUrlMap=(HashMap<String,Integer>)MapUtil.sortByValueDescending(SkipIInvokedMethodListener.serviceUrlMap);
	        Set<String> keySet = SkipIInvokedMethodListener.serviceUrlMap.keySet();
			//有了Set集合。就可以获取其迭代器。
			Iterator<String> it = keySet.iterator();
			boolean sm=true;
			while(it.hasNext()){
				String key = it.next();
				if(sm){
					cases="<font size='4' color='#FF0000'>流程错误列表</font><table border='1'><th>URL</th><th>错误次数</th>";
					bw.write(cases);
					bw.newLine();
					sm=false;
				}
				//有了键可以通过map集合的get方法获取其对应的值。
				Integer value  = SkipIInvokedMethodListener.serviceUrlMap.get(key);
				cases="<tr><td>"+key+"</td><td>错误了"+value+"次。</td></tr>";
				bw.write(cases);
				bw.newLine();
			}
			if(!sm){
				cases="</table>";
				bw.write(cases);
				bw.newLine();
				
			}
			
			boolean asd=true;
			Set<String> keySet1 = SkipIInvokedMethodListener.SkipMap.keySet();
			//有了Set集合。就可以获取其迭代器。
			Iterator<String> it1 = keySet1.iterator();
			while(it1.hasNext()){
				String key1 = it1.next();
				//有了键可以通过map集合的get方法获取其对应的值。
				String value1  = SkipIInvokedMethodListener.SkipMap.get(key1);
				String[] valuearr=value1.split("____");
				double v1=Double.valueOf(valuearr[0]);
				value1=valuearr[1];
				
				if(v1>Percentage){
					if(asd){
						cases="<font size='4' color='#FF0000'>场景错误列表</font><table border='1'><th>包含错误的场景名称</th><th>场景包含错误案例数量</th><th>错误占比</th>";
						bw.write(cases);
						bw.newLine();
						asd=false;
					}
					cases="<tr><td><font size='3' color='#0000CD'>"+key1+"</font></td>"
							+ "<td><font size='3' color='#0000CD'>"+value1+"</font></td>"
							+ "<td><font size='3' color='#0000CD'>"+v1+"%</font></td></tr>";
					bw.write(cases);
					bw.newLine();
				}
				
			}
			if(!asd){
				cases="</table>";
				bw.write(cases);
				bw.newLine();
			}
	        bw.flush();
	        bw.close();
	        return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	/**
	 * BufferedReader bufr = new BufferedReader(fr);
		

		String line = null;

		while((line=bufr.readLine())!=null)
		{
			System.out.print(line);
		}


		bufr.close();
	 */
	public static String readFile_line(String filename)throws IOException{
		
		File directory = new File(".");
        String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+filename;
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),"utf-8"));

		BufferedReader bufr = new BufferedReader(in);
		

		String line = null;
		StringBuffer sb=new StringBuffer();
		while((line=bufr.readLine())!=null)
		{
			sb.append(line);
		}


		bufr.close();
		in.close();
		return sb.toString();
		
	}
}
