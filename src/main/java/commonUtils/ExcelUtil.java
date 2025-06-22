package commonUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static String projectPath = System.getProperty("user.dir");

	public static void clearTestResultsColumn() {
		try (FileInputStream fis = new FileInputStream(testCasePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

			XSSFSheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			int testPassedColIndex = -1;

			for (Cell cell : headerRow) {
				if ("Is Test Passed".equalsIgnoreCase(cell.getStringCellValue().trim())) {
					testPassedColIndex = cell.getColumnIndex();
					break;
				}
			}

			if (testPassedColIndex == -1) {
				System.out.println("Column 'Is Test Passed' not found.");
				return;
			}

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;
				Cell resultCell = row.getCell(testPassedColIndex);
				if (resultCell != null) {
					resultCell.setCellValue("");
				}
			}

			try (FileOutputStream fos = new FileOutputStream(testCasePath)) {
				workbook.write(fos);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String testCasePath = projectPath + "\\Resources\\TestData.xlsx";

	public static List<String> getTestCasesToBeRun() {

		List<String> testCaseNames = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(testCasePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				Cell cell = row.getCell(2);

				if (cell.getStringCellValue().toString().equalsIgnoreCase("yes")) {
					testCaseNames.add(row.getCell(1).getStringCellValue().trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testCaseNames;
	}

	/*
	 * public static void updateTestResultsInExcel(String status, String testName)
	 * throws FileNotFoundException, IOException {
	 * 
	 * String startStr = ConfigReader.readProperty("starttime"); String endStr =
	 * ConfigReader.readProperty("endTime");
	 * 
	 * long startMillis = Long.parseLong(startStr); long endMillis =
	 * Long.parseLong(endStr);
	 * 
	 * long diffMillis = endMillis - startMillis;
	 * 
	 * long diffSeconds = diffMillis / 1000; long minutes = diffSeconds / 60; long
	 * seconds = diffSeconds % 60; System.out.println("Execution time: ");
	 * 
	 * System.out.println("Execution time: " + minutes + " minutes " + seconds +
	 * " seconds");
	 * 
	 * 
	 * try (FileInputStream fis = new FileInputStream(testCasePath); XSSFWorkbook
	 * workbook = new XSSFWorkbook(fis)) { XSSFSheet sheet = workbook.getSheetAt(0);
	 * 
	 * Row headerRow = sheet.getRow(0); int testPassedColIndex = -1; int
	 * executionTimeColIndex=-1; for (Cell cell : headerRow) { if
	 * ("Is Test Passed".equalsIgnoreCase(cell.getStringCellValue().trim())) {
	 * testPassedColIndex = cell.getColumnIndex();
	 * System.out.println(testPassedColIndex);
	 * 
	 * break; } } if (testPassedColIndex == -1) {
	 * System.out.println("Test Passed column not found"); return; }
	 * 
	 * 
	 * int testNameColIndex = 1; boolean found = false; for (Row row : sheet) { if
	 * (row.getRowNum() == 0) continue; Cell testNameCell =
	 * row.getCell(testNameColIndex); if (testNameCell != null &&
	 * testNameCell.getCellType() == CellType.STRING) {
	 * 
	 * String cellValue = testNameCell.getStringCellValue().trim(); if
	 * (cellValue.equalsIgnoreCase(testName)) {
	 * 
	 * Cell resultCell = row.getCell(testPassedColIndex); if (resultCell == null) {
	 * 
	 * resultCell = row.createCell(testPassedColIndex);
	 * 
	 * } resultCell.setCellValue(status);
	 * 
	 * try (FileOutputStream fos = new FileOutputStream(testCasePath)) {
	 * workbook.write(fos); } found = true; break; } } } if (!found) {
	 * System.out.println("Test case name '" + testName + "' not found."); return; }
	 * 
	 * } }
	 */
	public static void updateTestResultsInExcel(String status, String testName, String duration) throws IOException {
		String startStr = ConfigReader.readProperty(testName + "_starttime");
		String endStr = ConfigReader.readProperty("endTime");

		long startMillis = Long.parseLong(startStr);
		long endMillis = Long.parseLong(endStr);
		long diffMillis = endMillis - startMillis;

		long diffSeconds = diffMillis / 1000;
		long minutes = diffSeconds / 60;
		long seconds = diffSeconds % 60;
		String formattedTime = String.format("%02d:%02d", minutes, seconds);

		try (FileInputStream fis = new FileInputStream(testCasePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

			XSSFSheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			int testPassedColIndex = -1;
			int executionTimeColIndex = -1;

			for (Cell cell : headerRow) {
				String colName = cell.getStringCellValue().trim();
				if ("Is Test Passed".equalsIgnoreCase(colName)) {
					testPassedColIndex = cell.getColumnIndex();
				} else if ("Execution Time".equalsIgnoreCase(colName)) {
					executionTimeColIndex = cell.getColumnIndex();
				}
			}

			if (testPassedColIndex == -1 || executionTimeColIndex == -1) {
				System.out.println("Required column(s) not found in Excel.");
				return;
			}

			int testNameColIndex = 1;
			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;

				Cell testNameCell = row.getCell(testNameColIndex);
				if (testNameCell != null && testNameCell.getCellType() == CellType.STRING
						&& testName.equalsIgnoreCase(testNameCell.getStringCellValue().trim())) {

					Cell resultCell = row.getCell(testPassedColIndex);
					if (resultCell == null)
						resultCell = row.createCell(testPassedColIndex);
					resultCell.setCellValue(status);

					Cell timeCell = row.getCell(executionTimeColIndex);
					if (timeCell == null)
						timeCell = row.createCell(executionTimeColIndex);
					//timeCell.setCellValue(formattedTime);
					timeCell.setCellValue("'" + formattedTime);


					try (FileOutputStream fos = new FileOutputStream(testCasePath)) {
						workbook.write(fos);
					}

					return;
				}
			}

			System.out.println("Test case '" + testName + "' not found in Excel.");
		}
	}

	public static Map<String, Map<String, String>> getAllTestDataMap() {
		Map<String, Map<String, String>> allTestData = new HashMap<>();

		FileInputStream fis = null;
		XSSFWorkbook workbook = null;

		try {
			fis = new FileInputStream(testCasePath);

			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;

				Cell testCaseCell = row.getCell(1);
				Cell executeFlag = row.getCell(2);

				if (testCaseCell != null && executeFlag != null
						&& "yes".equalsIgnoreCase(executeFlag.getStringCellValue().trim())) {

					String testCaseName = testCaseCell.getStringCellValue().trim();
					Map<String, String> rowData = new HashMap<>();

					for (int col = 3; col < headerRow.getLastCellNum(); col++) {
						Cell headerCell = headerRow.getCell(col);
						Cell dataCell = row.getCell(col);

						if (headerCell != null) {
							String key = headerCell.getStringCellValue().trim();
							String value = (dataCell != null) ? dataCell.toString().trim() : "";
							rowData.put(key, value);
						}
					}

					allTestData.put(testCaseName, rowData);
				}
			}

		} catch (Exception e) {

			System.err.println("Workbook creation failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			e.printStackTrace();

		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}

				if (fis != null) {
					fis.close();
				}
			} catch (IOException closeEx) {
				closeEx.printStackTrace();
			}
		}

		return allTestData;
	}
}