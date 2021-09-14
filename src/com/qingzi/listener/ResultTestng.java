package com.qingzi.listener;



import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class ResultTestng implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> XmlSuite, List<ISuite> ISuite,
			String arg2) {
		TreeSet<Integer> treeSet=ProcessTestng.treeSet;
		Iterator<Integer> it=treeSet.iterator();
		StringBuffer sb=new StringBuffer();
		while(it.hasNext()){
			sb.append(it.next());
			sb.append(" ");
		}
		System.out.println("sb="+sb);
		// 创建文档。
		Document document = DocumentHelper.createDocument();
		// 设置文档DocType
		document.addDocType("suite", null, "http://testng.org/testng-1.0.dtd");
		// 文档增加节点，即根节点
		Element root = document.addElement("suite");
		root.addAttribute("name", "Failed suite-ys");
		// 根节点下添加节点
		Element first = root.addElement("test");
		// 节点添加属性
		first.addAttribute("name", "Default test-ys");
		// 节点下添加节点
		Element info = first.addElement("classes");
		Element class1 = info.addElement("class");
		class1.addAttribute("name", "com.puhui.test.RenMai_APITest");
		Element methods = class1.addElement("methods");
		Element include = methods.addElement("include");
		include.addAttribute("name", "f");
		include.addAttribute("invocation-numbers", sb.toString());

		XMLWriter writer =null;
		FileOutputStream fos =null;
		try {
			// 创建格式化类
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式，默认UTF-8
			format.setEncoding("UTF-8");
			// 创建输出流，此处要使用Writer，需要指定输入编码格式，使用OutputStream则不用
			fos = new FileOutputStream("src/DefectTest.xml");
			// 创建xml输出流
			writer = new XMLWriter(fos, format);
			// 生成xml文件
			writer.write(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos!=null)
					fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if(writer!=null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
