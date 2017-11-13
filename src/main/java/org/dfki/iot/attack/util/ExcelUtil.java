package org.dfki.iot.attack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dfki.iot.attack.model.RoverClientA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	// Create Logger
	private static final Logger myLogger = LoggerFactory.getLogger(ExcelUtil.class);

	public static void main(String[] args) {

		RoverClientA roverClientA1 = RoverClientA.getRandomRoverClientA();

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("sheet1");// creating a blank
															// sheet
		int rownum = 0;

		Row row = sheet.createRow(rownum++);
		createRow(roverClientA1, row);


		File directory = new File("./");
		   System.out.println(directory.getAbsolutePath());
		   
		try {
			FileOutputStream out = new FileOutputStream(new File("./src/main/resources/Data.xlsx"), true);
			workbook.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			
			FileInputStream excelFile = new FileInputStream(new File("./src/main/resources/Data.xlsx"));
			XSSFWorkbook workbook4Read = new XSSFWorkbook(excelFile);
			XSSFSheet datatypeSheet = workbook4Read.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				RoverClientA roverClientA = new RoverClientA();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					// getCellTypeEnum shown as deprecated for version 3.15
					// getCellTypeEnum ill be renamed to getCellType starting
					// from version 4.0

					if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
						System.out.println("CellType : " + currentCell.getCellType() + "  Value : "
								+ currentCell.getStringCellValue() );
					} else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						System.out.println("CellType : " + currentCell.getCellType() + "  Value : "
								+ currentCell.getNumericCellValue() );
					} else if (currentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						System.out.println("CellType : " + currentCell.getCellType() + "  Value : "
								+ currentCell.getBooleanCellValue() );
					}

				}
				System.out.println();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	private static void createRow(RoverClientA roverClientA1, Row row) {

		Cell cell = row.createCell(0);
		cell.setCellValue(roverClientA1.getId());

		cell = row.createCell(1);
		cell.setCellValue(roverClientA1.getSpeedSensorData());

		cell = row.createCell(2);
		cell.setCellValue(roverClientA1.getPercentagePowerAvilable());

		cell = row.createCell(3);
		cell.setCellValue(roverClientA1.isClientActive());
	}
}
