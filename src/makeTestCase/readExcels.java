package makeTestCase;



import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;




public class readExcels {
	
	public static void main(String[] args) {
		readExcels readExcels= new readExcels();
		List<List<String>> list=null;
		try {
			list=readExcels.readExcels_return("TestData/abc.xls","Sheet1");
//			String s=readExcels.readExcels_path("d:/abc.xls","Sheet1");
//			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//显示读取的数据
		for (List<String> list2 : list) {
			for (String string : list2) {
				System.out.print(string+" ");
				
			}
			System.out.println();
		}
	}
	//获取用例数据
	@SuppressWarnings({ "unused", "resource"})
	public List<List<String>> readExcels_return(String path,String Sheet) throws Exception{
		String targetFile = path;
		FileInputStream fis = new FileInputStream(new File(targetFile));
		Workbook wb = WorkbookFactory.create(new File(targetFile));
		Sheet sheet = wb.getSheet(Sheet);
		
		
		List<List<String>> listTestData = new ArrayList<List<String>>();
		List<String> listData =null;
		
		//反射测试数据对象内容到Object
		for(int i = 2 ; i < sheet.getPhysicalNumberOfRows() ; i++){
			Row r = sheet.getRow(i);			
			listData = new ArrayList<String>();
//			System.out.println("列="+r.getPhysicalNumberOfCells());
			for (int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
				Cell cell = r.getCell(j);
				String brandName = this.getCellValue(cell);
				if(brandName.indexOf("Sheet")!=-1){//如果在文件里找到了包含Sheet的字段就不存储这一行数据
					MakeTestCasesOld.SheetList.add(brandName);//记录那个sheet页面
					MakeTestCasesOld.nameList.add(this.getCellValue(r.getCell(1)));//保存包含sheet页面的字段名称
					++MakeTestCasesOld.state;	   //状态执为true，代表还需要循环读取其他sheet页面
					listData.clear();			   //清理这一行数据的存储
					break;
				}
				listData.add(brandName);
			}
			if(!listData.isEmpty()){
//				System.out.println("非空才存");
				listTestData.add(listData);
			}
//			System.out.println(listTestData.toString());
		}
//		System.out.println("readExcel结束未见异常");
		return listTestData;	
		
	}
	
	//获取接口地址
	@SuppressWarnings({ "unused", "resource"})
	public String readExcels_path(String path,String Sheet) throws Exception{
		String targetFile = path;
		FileInputStream fis = new FileInputStream(new File(targetFile));
		Workbook wb = WorkbookFactory.create(new File(targetFile));
		Sheet sheet = wb.getSheet(Sheet);
		
		String brandName ="";
		
		//反射测试数据对象内容到Object
		for(int i = 0 ; i < 1 ; i++){
			Row r = sheet.getRow(i);			
			for (int j = 0; j < 1; j++) {
				Cell cell = r.getCell(j);
				brandName = this.getCellValue(cell);
			}
		}
		return brandName;	
		
	}

	private String getCellValue(Cell cell){
		int cellType = cell.getCellType();
		String value = "";
		if(cellType == Cell.CELL_TYPE_STRING){
			value = cell.getStringCellValue();
		}else if(cellType == Cell.CELL_TYPE_NUMERIC){
			value = String.valueOf(cell.getNumericCellValue());
		}else if(cellType == Cell.CELL_TYPE_BOOLEAN){
			value = String.valueOf(cell.getBooleanCellValue());
		}else if(cellType == Cell.CELL_TYPE_BLANK){
			value = "";
		}else if(cellType == Cell.CELL_TYPE_FORMULA){
			value = String.valueOf(cell.getCellFormula());
		}else{
			value = "";
		}
		return value;
	}
	
	
}
