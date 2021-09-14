package test;

import com.qingzi.testUtil.SheetUtils;



public class w_excel {

	public static void main(String[] args) {
		 SheetUtils sheet = new SheetUtils("a1.xls", "Output");
		  sheet.writeExcel(
				  		"1",
				  		"2",
				  		"3",
				  		"4",
				  		"5",
				  		"6",
				  		"7",
				  		"8",
				  		"9",
				  		"10"
						);
	}

}
