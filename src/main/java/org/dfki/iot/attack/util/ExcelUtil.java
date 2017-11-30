package org.dfki.iot.attack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dfki.iot.attack.model.RoverAModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {

	private static final Logger myLogger = LoggerFactory.getLogger(ExcelUtil.class);

	public static void main(String[] args) {

		auditRequest("RoverA", JSONUtil.getJSONString(RoverAModel.getRandomRoverClientA()));
		readFromExcelFile("./src/main/resources/Data.xls", "RoverA");
	}

	private static void addRowEntryForRoverAModel(Sheet sheet, int rowNumber, RoverAModel roverAModel) {

		Row row = sheet.createRow(rowNumber);

		Cell cell = row.createCell(0);
		cell.setCellValue(new Date());

		cell = row.createCell(1);
		cell.setCellValue(roverAModel.getRequesterIPAddress());

		cell = row.createCell(2);
		cell.setCellValue(roverAModel.getRequesterMACAddress());

		cell = row.createCell(3);
		cell.setCellValue(roverAModel.getId());

		cell = row.createCell(4);
		cell.setCellValue(roverAModel.getSpeedSensorData());

		cell = row.createCell(5);
		cell.setCellValue(roverAModel.getPercentagePowerAvilable());

		cell = row.createCell(6);
		cell.setCellValue(roverAModel.isClientActive());

		cell = row.createCell(7);
		cell.setCellValue(roverAModel.getTempSensorData());

	}

	public static void readFromExcelFile(String filePath, String sheetName) {

		try {

			FileInputStream excelFile = new FileInputStream(new File(filePath));
			HSSFWorkbook workbook4Read = new HSSFWorkbook(excelFile);
			HSSFSheet datatypeSheet = workbook4Read.getSheet(sheetName);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
						System.out.print(" | " + currentCell.getStringCellValue());
					} else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						System.out.print(" | " + currentCell.getNumericCellValue());
					} else if (currentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						System.out.print(" | " + currentCell.getBooleanCellValue());
					}

				}
				System.out.println(" | ");
			}
			excelFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public static void writeToExcelFile(String filePath, String sheetName, RoverAModel objToWrite) {

		try {
			FileInputStream inputStream = new FileInputStream(new File(filePath));

			Workbook workbook4Read = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook4Read.getSheet(sheetName);

			int rownum = sheet.getLastRowNum() + 1;
			addRowEntryForRoverAModel(sheet, rownum, objToWrite);

			FileOutputStream outputStream = new FileOutputStream(new File(filePath));
			workbook4Read.write(outputStream);

			outputStream.close();
			inputStream.close();

		} catch (FileNotFoundException e) {
			myLogger.info(
					"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
							+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());

			myLogger.info(" The file not found, so creating new data file !! ");

			try {
				FileOutputStream outputStream = new FileOutputStream(new File(filePath));
				HSSFWorkbook workbook = new HSSFWorkbook();

				HSSFSheet sheet = workbook.createSheet(sheetName);
				sheet.autoSizeColumn(0);

				addHeaderEntryForRoverA(sheet, workbook, 0);

				addRowEntryForRoverAModel(sheet, 1, objToWrite);

				workbook.write(outputStream);
				outputStream.close();

			} catch (FileNotFoundException fnfe) {
				myLogger.info(
						"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
								+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
				e.printStackTrace();

			} catch (IOException ioe) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	private static void addHeaderEntryForRoverA(HSSFSheet sheet, HSSFWorkbook workbook, int rowNumber) {

		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		Row row = sheet.createRow(rowNumber);

		String[] headerArray = { " Request Date & Time ", "Requester IP Address", "Requester MAC Address", "ID",
				"Speed Sensor Data", "Percentage Power Avilable", "Client Active", "Temperature Sensor Data" };

		for (int i = 0; i < headerArray.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(headerArray[i]);
			cell.setCellStyle(style);
		}

	}

	public static void auditRequest(String appName, String roverAmodelAudit) {

		RoverAModel roverAmodel = (RoverAModel) JSONUtil.getObjFromJSONString(roverAmodelAudit, new RoverAModel());
		writeToExcelFile("./src/main/resources/Data.xls", appName, roverAmodel);

	}

}
