package com.qingzi.testUtil;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

public class Mail {
	public static void main(String[] args) {
		try {
			Mail.POP3();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public static void mailToAll(){
		try {
			Mail.POP3();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
//    public static String myEmailAccount = "53055975@qq.com";
//    public static String myEmailPassword = "qoqfoabljzyzbjdd";
	
    public static String myEmailAccount = "2507796383@qq.com";
    public static String myEmailPassword = "exmucrifufafdhfj";

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.qq.com";

    // 收件人邮箱（替换为自己知道的有效邮箱）
//    public static String receiveMailAccount = "liyongyu@offcn.com";//rmscrum.list@finupgroup.com
    public static String receiveMailAccount = "feifei.wu@matrx.team";//rmscrum.list@finupgroup.com

    public static void POP3() throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.host", myEmailSMTPHost);        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");   
//        props.setProperty("mail.stmp.port", "587");   
        props.setProperty("mail.stmp.port", "465");// 端口    
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(myEmailAccount, myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "接口自动化测试报告", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "", "UTF-8"));
        
        String overview=ReadHtml.readFile_("overview.html");
//      System.out.println(overview);
      
        String TestTitle="";
        String TestResule="";
      
        boolean isTrue=overview.contains("100.00%");
        int start=overview.indexOf("<td class=\"passRate\">");
        System.out.println(start);
        String Percentage=overview.substring(start+22, start+41);
        System.out.println(Percentage);
        if(isTrue){
        	message.setRecipients(MimeMessage.RecipientType.CC, parseAddress("onlyTest"));
        }else{
        	message.setRecipients(MimeMessage.RecipientType.CC, parseAddress("all"));
        }
      
        
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("wufeifei66504@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("liyongyu@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("lihong60540@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("yangfan60144@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("lifeifei58407@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("tangqingsong@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("zhouyeheng@offcn.com", "", "UTF-8"));
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("zhanghua54164@offcn.com", "", "UTF-8"));

        // 4. Subject: 邮件主题
        Date date=new Date();
        String time=new SimpleDateFormat("yyyyMMdd").format(date);
        message.setSubject("自动化测试报告-"+time, "UTF-8");

        /*
         * 下面是邮件内容的创建:
         */
//        message.setContent("定时任务--小雨直播接口自动化测试", "text/html;charset=UTF-8");
        
        // 5. 创建图片"节点"
//        MimeBodyPart image = new MimeBodyPart();
//        // 读取本地文件
//        DataHandler dh = new DataHandler(new FileDataSource("src\\mailTestPic.png"));
//        // 将图片数据添加到"节点"
//        image.setDataHandler(dh);
//        // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
//        image.setContentID("mailTestPic");    
//         
        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        
//        获取邮件内容原位置
//        String overview=ReadHtml.readFile_("overview.html");
////        System.out.println(overview);
//        
//        String TestTitle="";
//        String TestResule="";
//        
//        boolean isTrue=overview.contains("100.00%");
//        int start=overview.indexOf("<td class=\"passRate\">");
//        System.out.println(start);
//        String Percentage=overview.substring(start+22, start+41);
//        System.out.println(Percentage);
        
        
//        System.out.println("isTrue="+isTrue);
        if(isTrue){
        	TestTitle=" <font size='6' color='#00FF00'>&nbsp;&nbsp;&nbsp;&nbsp;"
        			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本次测试通过率:"+Percentage+"</font></br></br> ";
        }else{
        	TestTitle=" <font size='6' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;"
        			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本次测试通过率:"+Percentage+"</font> </br></br>";
        	TestResule=ReadHtml.readFile_("suite1_test1_results.html");
        }
        
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片+ReadHtml.readFile_("suite1_test1_results.html")
        text.setContent(TestTitle+"</br></br>"+ReadHtml.readFile_("overview.html")+"</br></br>"+TestResule, "text/html;charset=UTF-8");
//         
//        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
//        MimeMultipart mm_text_image = new MimeMultipart();
//        mm_text_image.addBodyPart(text);
//        mm_text_image.addBodyPart(image);
//        mm_text_image.setSubType("related");    // 关联关系
//         
//        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
//        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
//        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
//        MimeBodyPart text_image = new MimeBodyPart();
//        text_image.setContent(mm_text_image);
        
        MimeBodyPart attachment2 = new MimeBodyPart();
        
        File directory = new File(".");
    	String mailpath="";
        try {
        	mailpath = directory.getCanonicalPath() +File.separator+"test-output"+File.separator+"html\\";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(ZipUtils.ZipTestResult(mailpath)){
        	DataHandler dh2 = new DataHandler(new FileDataSource(mailpath+"测试报告详细资料.zip"));
        	attachment2.setDataHandler(dh2);
        	attachment2.setFileName("测试报告详细资料.zip"); 
//        	// 9. 创建附件"节点"
//        	MimeBodyPart attachment3 = new MimeBodyPart();
//        	MimeBodyPart attachment4 = new MimeBodyPart();
//        	MimeBodyPart attachment5 = new MimeBodyPart();
//        	MimeBodyPart attachment6 = new MimeBodyPart();
//        	MimeBodyPart attachment7 = new MimeBodyPart();
//        	// 读取本地文件
//        	DataHandler dh2 = new DataHandler(new FileDataSource("test-output\\html\\index.html"));
//        	DataHandler dh3 = new DataHandler(new FileDataSource("test-output\\html\\overview.html"));
//        	DataHandler dh4 = new DataHandler(new FileDataSource("test-output\\html\\reportng.css"));
//        	DataHandler dh5 = new DataHandler(new FileDataSource("test-output\\html\\reportng.js"));
//        	DataHandler dh6 = new DataHandler(new FileDataSource("test-output\\html\\suite1_test1_results.html"));
//        	DataHandler dh7 = new DataHandler(new FileDataSource("test-output\\html\\suites.html"));
//        	// 将附件数据添加到"节点"
//        	attachment2.setDataHandler(dh2);
//        	attachment3.setDataHandler(dh3);
//        	attachment4.setDataHandler(dh4);
//        	attachment5.setDataHandler(dh5);
//        	attachment6.setDataHandler(dh6);
//        	attachment7.setDataHandler(dh7);
//        	// 设置附件的文件名（需要编码）
//        	attachment2.setFileName("index.html");       
//        	attachment3.setFileName("overview.html");       
//        	attachment4.setFileName("reportng.css");       
//        	attachment5.setFileName("reportng.js");       
//        	attachment6.setFileName("suite1_test1_results.html");       
//        	attachment7.setFileName("suites.html");     
        }
        	
        
        
         
        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(attachment2);     // 如果有多个附件，可以创建多个多次添加
//        mm.addBodyPart(attachment3);     // 如果有多个附件，可以创建多个多次添加
//        mm.addBodyPart(attachment4);     // 如果有多个附件，可以创建多个多次添加
//        mm.addBodyPart(attachment5);     // 如果有多个附件，可以创建多个多次添加
//        mm.addBodyPart(attachment6);     // 如果有多个附件，可以创建多个多次添加
//        mm.addBodyPart(attachment7);     // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");         // 混合关系
 
        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        message.setContent(mm);
        //设置邮件的发送时间,默认立即发送
        message.setSentDate(new Date());

        return message;
    }
    
    
    public static InternetAddress[] parseAddress(String personnel){
    	String addr="";
    	if("all".equals(personnel)){
    		addr="qirui.si@matrx.team;feifei.wu@matrx.team;hongbin.wang@matrx.team;benben.shang@matrx.team;shujie.wang@matrx.team;";
//    		addr="feifei.wu@matrx.team;";
    	}else{
    		addr="feifei.wu@matrx.team;hongbin.wang@matrx.team;qirui.si@matrx.team;"
    				+ "yibo.ren@matrx.team;yaguang.jiang@matrx.team;benben.shang@matrx.team;shujie.wang@matrx.team;";
//    		addr="feifei.wu@matrx.team;";
    	}
    		
	    StringTokenizer token = new StringTokenizer(addr, ";");
	    InternetAddress[] addrArr = new InternetAddress[token.countTokens()];
	    int i = 0;
	    while (token.hasMoreTokens()){
	    	try{
	    		addrArr[i] = new InternetAddress(token.nextToken().toString());
		    }catch (AddressException e1){
		    	e1.printStackTrace();
		    	return null;
		    }
		    	i++;
		    }
	    return addrArr;
    }
    
}