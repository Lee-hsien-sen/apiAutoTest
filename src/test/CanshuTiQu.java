package test;

import java.util.Arrays;

public class CanshuTiQu {
	
	public static void main(String[] args) {
		String str="\"phone\":\"13910960649\",\"thirdSource\":\"GM\",\"thirdSourceId\":\"ys\",\"verifyCode\":code_own\"";
		String a=getAll(str,"verifyCode");
		System.out.println("a="+a);
		
	}

	// 在parameter中查看，是否有(第二个参数)verifyCode的关键字，有的话返回他的字母值
	public static String getAll(String parameter, String Letter) {
		String[] strcomma = parameter.split(",");
		int comma = strcomma.length;
		StringBuffer sb = new StringBuffer();
		for (int k = 0; k < comma; k++) {
			// 此时是多个，，，
			String[] str = strcomma[k].split(":");
			String str_strcomma = Arrays.toString(str);
			// System.out.println("str="+Arrays.toString(str));
			// 按参数传过来的字符串做为子串，在以逗号为节点的串中分别查找子串的关键字，
			// 在找到后的位置开始查找数字，最后把数字的字符串返回
			if (str_strcomma.contains(Letter)) {
				int start = str_strcomma.indexOf(',');
				sb.append(str_strcomma.substring(start + 2,
						str_strcomma.length() - 1));
			}
		}
		return sb.toString();
	}
}
