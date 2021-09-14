package testData;

import org.testng.annotations.DataProvider;

import com.qingzi.testUtil.ReadExcels;
import com.qingzi.testUtil.ReadProperties;


public class offcn_api_testData {
	public static int Testcount=0;//总得用力数量
	
	  @DataProvider(name="renmai")
	  public static Object[][] dp(){
		  String xls=ReadProperties.GetTestPropertyByKey("xls");
		  String sheet=ReadProperties.GetTestPropertyByKey("sheet");
		  ReadExcels readExcels = new ReadExcels(xls,sheet);
		  try {
			return readExcels.readExcels_return();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return null;
	  }

}
