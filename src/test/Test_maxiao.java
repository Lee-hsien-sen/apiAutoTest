package test;

public class Test_maxiao {

	public static void main(String[] args) {
		System.out.println(removeFourChar("えもじ,e-moji,moj"));
		
	}
	/**
	 * 替换四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）的解决方案
	 *
	 * @param content
	 * @return
	 * @author 张栋
	 * @data 2015年8月11日 上午10:31:50
	 */
	public static String removeFourChar(String content) {
	    byte[] conbyte = content.getBytes();
	    System.out.println(conbyte[0]);
	    for (int i = 0; i < conbyte.length; i++) {
	        if ((conbyte[i] & 0xF8) == 0xF0) {
	            for (int j = 0; j < 4; j++) {
	            	System.out.println("j="+j);
	                conbyte[i + j] = 0x30;
	            }
	            i += 3;
	        }
	    }
	    String contentnew = new String(conbyte);
	    return contentnew.replaceAll("0000", "");
	}

}
