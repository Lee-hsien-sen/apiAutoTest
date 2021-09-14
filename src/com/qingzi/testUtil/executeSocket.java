package com.qingzi.testUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author 吴飞飞
 *模拟socket通信发起tcp接口请求
 */
public class executeSocket {
	
	public static String host="192.168.200.103";
	public static int port= 9015;
	
	public static String execute(String host, int port, String request){
		long begin = System.currentTimeMillis();
		Socket socket =null;
		DataInputStream input = null;
		try {
			socket = new Socket();
			System.out.println("send++++++++");
			System.out.println("请求字符串"+ request);
			//打开输出流
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			//发送字节数组流长度
			output.writeInt(request.getBytes("utf-8").length);
			//发送字节数组流内容
			output.write(request.getBytes("utf-8"));
			output.flush();
			//打开输出流
			input = new DataInputStream(socket.getInputStream());
			int length = input.readInt();
			byte[] bytes = new byte[length];
			//读取服务端返回的字节数组流
			input.readFully(bytes,0,length);
			long end = System.currentTimeMillis();
			System.out.println("total time:" + (end - begin) + "ms");
			return new String(bytes,"UTF-8");
 			
		} catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
			
		}finally{
			try {
				if(socket !=null){
					input.close();
					socket.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String ss="{\"id\":\"1626089856795\",\"method\":\"bind\",\"request\":true,\"data\":{\"token\": \"396fe378bbaf87677ce4b31c1dae6bbbd945cf263ce369dc4e0cc5cd67be920f\",\"peerId\": \"1011003599789395\",\"roomId\": \"1001926117966\"}}";
		
		System.out.println(execute(host, port, ss));
	}

}
