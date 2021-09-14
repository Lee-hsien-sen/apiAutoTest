package com.qingzi.testUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.*;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SheetUtils {

	public Workbook workbook;
	public Sheet sheet;
	public Cell cell;
	int rows;
	int columns;
	public String fileName;
	public String sheetName;
	public ArrayList<String> arrkey = new ArrayList<String>();
	String sourceFile;

	/**
	 * @param fileName
	 *            excel鏂囦欢鍚�
	 * @param caseName
	 *            sheet鍚�
	 */
	public SheetUtils(String fileName, String sheetName) {
		super();
		this.fileName = fileName;
		this.sheetName = sheetName;
	}

	public Object[][] getExcelData() {

		try {
			workbook = Workbook.getWorkbook(new File(getPath()));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheet(sheetName);
		rows = sheet.getRows();
		columns = sheet.getColumns();
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[][] arrmap = new HashMap[rows - 1][1];
		if (rows > 1) {
			for (int i = 0; i < rows - 1; i++) {
				arrmap[i][0] = new HashMap<>();
			}
		} else {
			System.out.println("excel no data");
		}

		for (int c = 0; c < columns; c++) {
			String cellvalue = sheet.getCell(c, 0).getContents();
			arrkey.add(cellvalue);
		}
		for (int r = 1; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				Object cellvalue = getCellValue(c, r);
				if (cellvalue != null)
					arrmap[r - 1][0].put(arrkey.get(c), getCellValue(c, r));
			}
		}
//		for(int i=0;i<arrmap.length;i++){
//			for(int j=0;j<arrmap[i].length;j++){
//				System.out.println("i="+i+",j="+j);
//				System.out.println("arr="+arrmap[i][j]);
//			}
//		}
		return arrmap;
	}

	public Object getCellValue(int c, int r) {
		Object cellvalue = null;
		Cell cell = sheet.getCell(c, r);
		if (cell.getContents().equals("null")) {
			return null;
		}
		if (sheet.getCell(c, 0).getContents().equals("list")) {

		}
		if (cell.getType() == CellType.LABEL) {
			LabelCell nc = (LabelCell) cell;
			return nc.getString();

		}
		if (cell.getType() == CellType.DATE) {
			DateCell nc = (DateCell) cell;
			return nc.getDate();
		}

		if (cell.getType() == CellType.NUMBER) {
			NumberCell nc = (NumberCell) cell;
			return nc.getValue();
		}

		return cellvalue;
	}

	/**
	 * 鑾峰緱excel鏂囦欢鐨勮矾寰�
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getPath() throws IOException {
		sourceFile = "TestData/"+fileName;
		return sourceFile;
	}

	public void writeExcel(String NO,String caseNo, String input,String Description, 
			String statusline, String resbody,String codeORerrcode,String msgORerrmsy,
			String result,String time) {
		File file;
		try {
//			System.out.println(getPath());
			file = new File(getPath());
			Workbook rwb = Workbook.getWorkbook(file);
			File tempfile = new File("TestData/"+fileName);
			WritableWorkbook wwb = Workbook.createWorkbook(tempfile, rwb);
			WritableSheet ws = wwb.getSheet(sheetName);
			if (ws == null) {
				ws = wwb.createSheet(sheetName, 0);
			}

			int rows = ws.getRows();

			WritableFont wf = new WritableFont(WritableFont.TAHOMA);
			wf.setPointSize(10);
			WritableCellFormat wcf = new WritableCellFormat(wf);

			ws.addCell(new Label(0, rows, NO, wcf));
			ws.addCell(new Label(1, rows, caseNo, wcf));
			ws.addCell(new Label(2, rows, input, wcf));
			ws.addCell(new Label(3, rows, Description, wcf));
			ws.addCell(new Label(4, rows, statusline, wcf));
			ws.addCell(new Label(5, rows, resbody, wcf));
			ws.addCell(new Label(6, rows, codeORerrcode, wcf));
			ws.addCell(new Label(7, rows, msgORerrmsy, wcf));
			ws.addCell(new Label(8, rows, time, wcf));

			if (result.equals("Pass")) {
				wf = new WritableFont(WritableFont.TAHOMA);
				wcf = new WritableCellFormat(wf);
				wf.setColour(Colour.GREEN);
			} else if (result.contains("Fail")) {
				wf = new WritableFont(WritableFont.TAHOMA);
				wcf = new WritableCellFormat(wf);
				wf.setColour(Colour.RED);
			} else {
				wf = new WritableFont(WritableFont.TAHOMA);
				wcf = new WritableCellFormat(wf);
				wf.setColour(Colour.YELLOW);
			}

			ws.addCell(new Label(9, rows, result, wcf));
			wwb.write();
			wwb.close();
			rwb.close();
			file.delete();
			tempfile.renameTo(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeExcel(String c1, String c2, String c3) {
		File file;
		try {
			file = new File(getPath());
			Workbook rwb = Workbook.getWorkbook(file);
			File tempfile = new File("TestData/"+fileName);
			WritableWorkbook wwb = Workbook.createWorkbook(tempfile, rwb);
			WritableSheet ws = wwb.getSheet(sheetName);
			if (ws == null) {
				ws = wwb.createSheet(sheetName, 0);
			}

			int rows = ws.getRows();
			ws.addCell(new Label(0, rows, c1));
			ws.addCell(new Label(1, rows, c2));
			ws.addCell(new Label(2, rows, c3));
			wwb.write();
			wwb.close();
			rwb.close();
			file.delete();
			tempfile.renameTo(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cleanAndWriteToSheet(String[][] array) {
		File file;
		try {
			file = new File(getPath());
			Workbook rwb = Workbook.getWorkbook(file);
			File tempfile = new File("TestData/"+fileName);
			WritableWorkbook wwb = Workbook.createWorkbook(tempfile, rwb);
			WritableSheet ws = wwb.getSheet(sheetName);
			if (ws == null) {
				ws = wwb.createSheet(sheetName, 0);
			}

			int rows = ws.getRows();
			for (int i = 0; i < rows - 1; i++) {
				ws.removeRow(i);
			}
			for (int r = 0; r < array.length - 1; r++) {
				for (int c = 0; c < array[r].length - 1; c++) {
					ws.addCell(new Label(c, r, array[r][c]));
				}
			}
			wwb.write();
			wwb.close();
			rwb.close();
			file.delete();
			tempfile.renameTo(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
