package com.demo.utlility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtils {
	
	public static XSSFSheet ExcelWSheet;
	public static XSSFWorkbook ExcelWBook;
	public static XSSFCell Cell;
	public static XSSFRow Row;
	// Data provider
	//@SuppressWarnings("unchecked")
	public static <UnicodeString> UnicodeString getCellData(int RowNum, int ColNum) throws Exception {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			int dataType = Cell.getCellType();
			if (dataType == 3) {
				return (UnicodeString) "";
			} else {
				DataFormatter formatter = new DataFormatter();
				UnicodeString Data = (UnicodeString) formatter.formatCellValue(Cell);
				return Data;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}
	public static String getCellValue(String datafile,String sheet,int i , int j)
	{
		String ser=null;
		try {
			FileInputStream ExcelFile = new FileInputStream(datafile);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheet);
			ser = getCellData(i, j);
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ser;
	}
	
	public static void setCellData(String DataFile,String sheet, String Result,  int RowNum, int ColNum) throws Exception  
	 {
	  try
	  {
		  FileInputStream ExcelFile = new FileInputStream(DataFile);
		  ExcelWBook = new XSSFWorkbook(ExcelFile);
		  ExcelWSheet = ExcelWBook.getSheet(sheet);
	   Row  = ExcelWSheet.getRow(RowNum);
	   Cell = Row.getCell(ColNum);
	   if (Cell == null) 
	   {
	    Cell = Row.createCell(ColNum);
	    Cell.setCellValue(Result);
	   } 
	   else 
	   {
	    Cell.setCellValue(Result);
	   }
	   
	   /*Constant variables Test Data path and Test Data file name*/
	   FileOutputStream fileOut = new FileOutputStream(DataFile);
	   ExcelWBook.write(fileOut);
	   fileOut.flush();
	   fileOut.close();
	  }
	  catch(Exception e)
	  {
	   throw (e);
	  }
	 }
	public static String getCellValue(String sheet,int i , int j)
	{
		String ser=null;
		try {
			String dataFilePath =  System.getProperty("user.dir") + "/src/main/java/com/green/rfp/qa/testdata/login.xlsx";
			//System.out.println(dataFilePath);
			File datafile = new File(dataFilePath);
			String fullpath = datafile.getAbsolutePath();
			FileInputStream ExcelFile = new FileInputStream(fullpath);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheet);
			ser = getCellData(i, j);
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ser;
	}
	

}
