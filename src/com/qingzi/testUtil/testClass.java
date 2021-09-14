/**  
 * @Description:   
 * @author: wff    
 * @date: 2020年5月22日 下午5:40:28   
 * @version V1.0 
 */  
package com.qingzi.testUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;
import sun.security.jca.GetInstance.Instance;
import test.map_show;

import com.sun.jna.StringArray;

/**  
 * @Description:   
 * @author: wff    
 * @date: 2020年5月22日 下午5:40:28   
 * @version V1.0 
 */
public class testClass {
	
	//判断一个字符串是否是一个回文
	public static void main1(String[] args) {
		String str = "level";
		StringBuffer sb = new StringBuffer(str);
		sb.reverse();
		int count = 0;
		
		for(int i=0; i<str.length();i++){
			if (str.charAt(i) == sb.charAt(i)) {
				count ++;
			}
		}
		if(count == str.length()){
			System.out.println("这是一个回文字符串");
		}else {
			System.out.println("这不是");
		}
	}
	//冒泡排序
	public static void main2(String[] args) {
		int[] arr = new int[]{2,4,6,7,9,8,1,3,5};
		
		for(int i=0; i<arr.length;i++){
			for (int j = 0; j < arr.length-1-i; j++) {
				if (arr[j]>arr[j+1]) {
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i] + "\t");
		}
		
 	}
	
	//查找字符串中重复的字符及出现的次数
	public static void main3(String[] args) {
		String str = "sadflkjsadkfnjciend";
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for(int i=0;i<str.length();i++){
			if(map.get(str.charAt(i)) !=null){
				map.put(str.charAt(i), map.get(str.charAt(i))+1);
			}
			else {
				map.put(str.charAt(i), 1);
			}
		}
		System.out.println(map);
	}
	//查找数组中的重复元素与次数
	public static void main4(String[] args) {
		int[] array = {1, 2, 5, 5, 5, 5, 6, 6, 7, 2, 9, 2};
        Map<Integer,Integer> map = findRepetition(array);
        if(map!=null){
            for (Map.Entry<Integer,Integer> entry : map.entrySet()){
                if(entry.getValue()>1){
                    System.out.println("元素 "+entry.getKey()+" 重复出现"+entry.getValue()+"次");
                }else{
                    System.out.println("元素 "+entry.getKey()+" 只出现1次 无重复");
                }
            }
        }
	}
	
	private static Map<Integer, Integer> findRepetition(int[] arr){
        Map<Integer, Integer> map = new HashMap<>();
        if(arr == null || arr.length <= 0){
            return null;
        }
        for(int i = 0; i < arr.length; i ++){
            if(map.containsKey(arr[i])){
                map.put(arr[i], map.get(arr[i])+1);
            }else{
                map.put(arr[i], 1);
            }
        }
        return map;
    }
	//10以内的质数
	public static void main5(String[] args) {
		
		for(int i=2;i<=100;i++){
			boolean zhushu = true;
			for(int j=2;j<i;j++){
				if(i%j == 0){
					zhushu = false;
					break;
				}
			}
			if(zhushu){
				System.out.print(i +" ");
			}
		}

		}
	//给出一个数组求出两元素相加等于指定元素
	public static void main6(String[] args){
		int arr[] ={1,2,3,4,5,6,7,8,9};
		for(int i=0;i<arr.length-1;i++){
			for(int j=i+1;j<arr.length;j++){
				if(arr[i]+arr[j] == 10){
					System.out.println(arr[i]+ "+"+arr[j] +"=10");
				}
			}
		}
	}
	//恶汉模式
//	@SuppressWarnings("unused")
//	private static Singleton instance;
//	
//	@SuppressWarnings("unused")
//	private void Singleton(){}
//	
//	public static synchronized Singleton getinstance(){
//		if (instance == null) {
////			    instance =  new Singleton() ;
//			}
//		
//		return instance;
//	}
	//懒汉模式
//	private static testClass testClass = new testClass();
//	
//	private void testClass(){};
//	
//	public static testClass getTestClass(){
//		
//		return testClass;
//	}
	
	//一个多位数各位数字相加直至和为一位数
	public static void main7(String[] args) {
		int num = 123;
		
		int num1 = 0, num2 = 0;
		while (num > 9) {
			num1 = num % 10;
			num2 = num / 10;
			num = num1 + num2; 
		}
		System.out.println(num);
	}
	//给出一个int类型123，写一个函数，返回反转的值321 
	public static void main8(String[] args){
		int num = 123;
		String strnum = String.valueOf(num);
		StringBuffer sb = new StringBuffer(strnum);
		sb.reverse();
		String re = sb.toString();
		Integer in = Integer.parseInt(re);
		System.out.println(in);
	}
	
	public static void main9(String[] args) {
		System.out.println(new Random().nextInt(9000)+ 1000);
	}
	
	public static void main10(String[] args) {
		HashMap<String, String> userMap = new HashMap<String, String>();
		userMap.put("dev", "1");
		userMap.put("userAccountId", "sdsd");
		ArrayList<Object> userList = new ArrayList<Object>();
		userList.add(userMap);
//		System.out.println(JSONObject.fromObject(userList));
		System.out.println(JSONObject.fromObject(userMap));
	}
	
	
}
	

