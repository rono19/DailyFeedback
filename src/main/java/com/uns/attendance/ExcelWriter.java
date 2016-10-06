package com.uns.attendance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	
	private Properties prop;
	
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public Properties getProp() {
		return prop;
	}
	
	private static ExcelWriter excelWriter = new ExcelWriter();
	
	public static ExcelWriter getInstance() {
		return excelWriter;
	}
	
	/**
	 * accessing the in-memory data structure to read the data and write it into the newly formed excel
	 * @param data
	 * @throws IOException
	 */
	public void writeDataIntoFinalExcel(Map<String, List<StudentInfo>> data) throws IOException {
		
		//Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderFormatting.BORDER_THIN);
        headerStyle.setBorderLeft(BorderFormatting.BORDER_THIN);
        headerStyle.setBorderRight(BorderFormatting.BORDER_THIN);
        headerStyle.setBorderTop(BorderFormatting.BORDER_THIN);
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderFormatting.BORDER_THIN);
        dataStyle.setBorderLeft(BorderFormatting.BORDER_THIN);
        dataStyle.setBorderRight(BorderFormatting.BORDER_THIN);
        dataStyle.setBorderTop(BorderFormatting.BORDER_THIN);
        dataStyle.setWrapText(true);
         
        for(String date : data.keySet()) {
        	
        	//Create a blank sheet
            XSSFSheet sheet = workbook.createSheet(date);
            List<StudentInfo> students = data.get(date);
            int COLUMNS = FinalSheetFields.values().length;
            int rowNum = 0;
            
            // for rows
            for(StudentInfo student : students) {
            	
            	Row row = sheet.createRow(rowNum);
            	if(rowNum == 0) {
            		int cellNum = 0;
            		//for individual cell
            		for(int i=0 ; i< COLUMNS ; i++) {
            			Cell cell = row.createCell(cellNum++);
            			cell.setCellStyle(headerStyle);
            			FinalSheetFields finalSheetField = FinalSheetFields.getEnumByPos(i);
            			cell.setCellValue(finalSheetField.getField());
            		}
            	} else {
            		String[] values = populateStudentAsStringArrayInOrder(student, COLUMNS);
            		//for individual cell
            		int cellNum = 0;
            		for(int i=0 ; i<values.length ; i++) {
            			Cell cell = row.createCell(cellNum++);
            			cell.setCellStyle(dataStyle);
            			cell.setCellValue(values[i]);
            		}
            	}
            	
            	rowNum++;
            }
        	
        }
        
        //Write the workbook in file system
        FileOutputStream out = new FileOutputStream(new File(prop.getProperty(Constants.FILE_WRITE_PATH)));
        workbook.write(out);
        out.close();
        System.out.println("New File formed successfully and data entered.");
		
	}

	/**
	 * Convert pojo into string array maintaining order.
	 * TODO This should be done in a proper manner - otherwise, it is difficult to maintain order.
	 * @param student
	 * @param COLUMNS
	 * @return
	 */
	private String[] populateStudentAsStringArrayInOrder(StudentInfo student, final int COLUMNS) {
		CommonColumns commonColumns = student.getCommonColumns();
    	String studentName = student.getName();
    	String attendance = student.isPresent() == true ? "Y" : "N";
    	String teacher = commonColumns == null? null : commonColumns.getTeacher();
    	String topics = commonColumns == null? null : commonColumns.getTopics();
    	String homework = commonColumns == null? null : commonColumns.getHomeworkGiven();
    	String plansForNextDay = commonColumns == null? null : commonColumns.getPlansForNextday();
    	String remarks = student.getRemarks();
    	String activities = commonColumns == null? null : commonColumns.getActivitiesPerformed();
    	
    	String[] values = new String[COLUMNS];
    	values[0] = studentName;
    	values[1] = attendance;
    	values[2] = teacher;
    	values[3] = topics;
    	values[4] = homework;
    	values[5] = plansForNextDay;
    	values[6] = remarks;
    	values[7] = activities;
    	
    	return values;
	}

}
