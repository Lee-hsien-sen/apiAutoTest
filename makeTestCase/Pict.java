package makeTestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.offcn.TestUnti.cmdUtil;
import com.offcn.TestUnti.fileUntil;

public class Pict {

	public Set<String> GenerateCases(List<Set<String>> listt,String kelidu){
//		System.out.println(listt);
		fileUntil fu=new fileUntil();
		if(fu.WriterFile(listt)){  //写入文件
//			System.out.println("写入成功");
			if(mackOutcases(kelidu)){    //调用pict程序生成txt文件
				return trueCases();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		mackOutcases("3");
	}
	
	//根据文件生成正向用例
	public static Set<String> trueCases(){
		String [] title=null;
		Set<String> set=new HashSet<String>(); 
		try {
			ArrayList<String []> list=readFile_("caseOut.txt");
			for(int i=0;i<list.size();i++){
				if(i==0){
					title=list.get(i);
				}else{
					String KeyAndValue="";
					for(int j=0;j<list.get(i).length;j++){
						String [] value=list.get(i);
						KeyAndValue=KeyAndValue+"\""+title[j]+"\":"+value[j]+",";
					}
					set.add(KeyAndValue.substring(0,KeyAndValue.length()-1));
				}
//				System.out.println(set);
			}
			return set;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String []> readFile_(String filename)throws IOException{
		
		File directory = new File(".");
        String sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+filename;
		
		//创建一个读取流对象和文件相关联。
		FileReader fr = new FileReader(sourceFile);

		//为了提高效率。加入缓冲技术。将字符读取流对象作为参数传递给缓冲对象的构造函数。
		BufferedReader bufr = new BufferedReader(fr);

		String line = null;

		ArrayList<String []> list=new ArrayList<String[]>();
		while((line=bufr.readLine())!=null){
			System.out.println(line);
			String []title =line.split("\t");
			list.add(title);
		}

		bufr.close();
	
		return list;
	}
	
	public static boolean  mackOutcases(String kelidu){
		try {
        	File directory = new File(".");
            String sourceFile;
            String sourceFile1;
    		sourceFile = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator+"case.txt";
    		sourceFile1 = directory.getCanonicalPath() +File.separator+"src"+File.separator+"resources"+File.separator;
        	String cmd = "pict "+sourceFile+">"+sourceFile1+"caseOut.txt";
            String cmd2 = "cmd /c  " + "\"\" " + cmd ;
            if(kelidu!=null){
            	cmd2=cmd2+" "+"/o:"+kelidu;
    		}
//            System.out.println(cmd2);
            cmdUtil.run(cmd2);
            return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
