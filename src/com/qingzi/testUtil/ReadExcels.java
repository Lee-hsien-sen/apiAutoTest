package com.qingzi.testUtil;

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
		Log.logInfo("数据源读取记录：共有几列数据="+list.size());
		Log.logInfo("数据源读取记录：共有几行数据="+arrmap.length);
		for(int i = 1 ; i < sheet.getPhysicalNumberOfRows() ; i++){
			Row r = sheet.getRow(i);
			for (int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
				Cell cell = r.getCell(j);
				String brandName=getCellValue(cell);
				if (!brandName.equals("null")){//如果单元格不等于空，才进行存储
//					System.out.println("i="+i+",j="+j);
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
		return arrmap;

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
