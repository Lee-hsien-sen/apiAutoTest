package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Decoder;
/**
 * 验证码识别类
 * 第一个参数：几位的中英数
 * 第二个参数：图片地址
 * 返回解析好的字符串
 * @author puhui
 */
public class aliOCR {
	
	
	public static void main(String[] args) {
//		String YanZhengMa=getYZM(4,"d:\\t111.jpg");
////		getYZM(4,"d:\\abc.jpg");
//		System.out.println("验证码="+YanZhengMa);
		
		//将字符串转换成图片
		boolean b=GenerateImage("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");
		System.out.println(b);
	}
	
	
	
	public static String getYZM(int count,String imagePath){
		String host = "http://ali-checkcode.showapi.com";
		String path = "/checkcode";
		String method = "POST";
		Map<String, String> headers = new HashMap<String, String>();
		//最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE 4afc3a5a191a4bbd80b837c3374ac872");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		bodys.put("convert_to_jpg", "0");
		bodys.put("img_base64", encodeImgageToBase64(new File(imagePath)));
		bodys.put("typeId", "30"+count+"0");
		String result=null;
		try {
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			result=EntityUtils.toString(response.getEntity());
			JSONObject obj = JSONObject.fromObject(result); 
			result=obj.getString("showapi_res_body");
			JSONObject obj1 = JSONObject.fromObject(result); 
			result=obj1.get("Result").toString();
			System.out.println("获取的验证码是："+obj1.get("Result"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String encodeImgageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        // 其进行Base64编码处理  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            InputStream in = new FileInputStream(imageFile);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return new String(Base64.encodeBase64(data));
      
    }  
	
	 //base64字符串转化成图片  
    public static boolean GenerateImage(String imgStr)  
    {   //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
            //生成jpeg图片  
            String imgFilePath = "d://222.jpg";//新生成的图片  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  

    }
}
