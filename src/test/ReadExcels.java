package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.qingzi.testUtil.Log;



public class ReadExcels {
	private  String fileName;
	private  String SheetName;
	
	public ReadExcels() {
	}
	public ReadExcels(String fileName, String sheetName) {
		this.fileName = fileName;
		SheetName = sheetName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSheetName() {
		return SheetName;
	}
	public void setSheetName(String sheetName) {
		SheetName = sheetName;
	}
	
	@SuppressWarnings({ "unused", "resource" })
	public Object[][] readExcels_return() throws Exception{
		String targetFile = "TestData/"+fileName;
		FileInputStream fis = new FileInputStream(new File(targetFile));
		Workbook wb = WorkbookFactory.create(new File(targetFile));

		Sheet sheet = wb.getSheet(SheetName);
		int rows=sheet.getPhysicalNumberOfRows();
		
		//有多少行数据就创建多少个map，首行是标题第二行开始才是数据，所以rows-1
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[][] arrmap = new HashMap[rows-1][1];
		List<String> list = new ArrayList<String>();
		
		//每个子map分别为arrmap[0][0]、arrmap[1][0]、arrmap[2][0]。。。
		for(int i = 1 ; i < sheet.getPhysicalNumberOfRows() ; i++){
			arrmap[i-1][0] = new HashMap<>();
		}
		//获取标题行数据存放在list里面
		for(int i = 0 ; i < 1 ; i++){
			Row r = sheet.getRow(i);
			for (int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
				Cell cell = r.getCell(j);
				list.add(getCellValue(cell));
			}
		}
		
		for(int i = 1 ; i < sheet.getPhysicalNumberOfRows() ; i++){
			Row r = sheet.getRow(i);
			for (int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
				Cell cell = r.getCell(j);
				String brandName=getCellValue(cell);
				//如果列名是parameter的时候，按逗号分隔字符串
				if("parameter".equals(list.get(j))){
					
					System.out.println("看看="+brandName);
					arrmap[i - 1][0].put(list.get(j), brandName);//分别往每个子map中存放数据，每行是一个map
//					String[] strcomma=brandName.split(",");
//					int comma=strcomma.length;
//					for(int k=0;k<comma;k++){
//						String[] str=strcomma[k].split(":");
//						System.out.println("str="+Arrays.toString(str));
//						if(str.length>1){
//							arrmap[i - 1][0].put((String)filterString(str[0].trim(),i,j),filter(str[1],i,j));//分别往每个子map中存放数据，每行是一个map
////							arrmap[i - 1][0].put((String)(str[0].trim()),(str[1]));//分别往每个子map中存放数据，每行是一个map
//						}
//					}
				}else{
					arrmap[i - 1][0].put(list.get(j), brandName);//分别往每个子map中存放数据，每行是一个map
				}
			}
			
		}
		/**
		 * 查看数据提取结果
		 
		for(int i=0;i<arrmap.length;i++){
			for(int j=0;j<arrmap[i].length;j++){
				System.out.print("  "+arrmap[i][j]);
			}
			System.out.println();
		}
		for(int i=0;i<arrmap.length;i++){
			HashMap<String, Object> arr=arrmap[i][0];
			System.out.println("处理后数据="+JSONObject.fromObject(arr).toString());
		}
		*/
		System.out.println(arrmap);
		return arrmap;

	}
	//去掉字符串的双引号,row行，j列
	private String filterString(String str,int rows,int j){
		if(str==null){
			Log.logError("readExcels filterString error"+str+"发生在"+rows+"行"+j+"列");
			return "null";
		}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<str.length();i++){
			char c=str.charAt(i);
			if(str.charAt(i)=='"'){
			}else{
				sb.append(c);
			}
		}
		return sb+"";
	}
	
	//输入类型转换成数字型,row行，j列
	private Long filterInt(String str){
		return Long.valueOf(str);
	}
	
	//选择何种转换方式
	private Object filter(String str,int i,int j){
		System.out.println(111);
		System.out.println(str);
		if(str.equals("null")){
			System.out.println(1234);
			return "null";
		}
		if(str.equals("\"null\"")){
			System.out.println(5678);
			return "\"null\"";
		}
		if(!Character.isDigit(str.charAt(0))){//如果首位不是数字就按字符串处理
			return filterString(str, i, j);
		}else{
//			return filterInt(str);
			return "";
		}
	}
		
	private String getCellValue(Cell cell){
		int cellType=0;
		try {
			cellType = cell.getCellType();
		} catch (Exception e) {
			return "无法解析";
		}
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
