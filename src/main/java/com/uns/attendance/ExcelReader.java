package com.uns.attendance;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

public class ExcelReader {
	
	private Properties prop;
	
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public Properties getProp() {
		return prop;
	}
	
	private static ExcelReader excelReader = new ExcelReader();
	
	private ExcelReader() {}
	
	public static ExcelReader getInstance() {
		return excelReader;
	}
	
	/**
	 * read from the response excel and create an in-memory data structure to store the data
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<StudentInfo>> readResponseExcelAndCreateData() throws IOException {
		
		FileInputStream file = new FileInputStream(new File(prop.getProperty(Constants.FILE_READ_PATH)));
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
      //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        final int ROW_COUNT = sheet.getPhysicalNumberOfRows();
        int COLUMN_COUNT = 0;
        Map<String, List<StudentInfo>> responseData = new HashMap<>();

        //Iterate through each rows one by one
        for(int i=0 ; i<ROW_COUNT ; i++) {
        	Row row = sheet.getRow(i);
        	
        	switch(i) {
        	//header row
        	case 0:
        		COLUMN_COUNT = row.getPhysicalNumberOfCells();
        		break;
        	//other rows
        	default:
        		modifyDataFormatForExcel(responseData, row, COLUMN_COUNT);
        	}
        }
        
        file.close();
		return responseData;
	}
	
	/**
	 * modify data from response excel to write to the new excel
	 * @param excelData
	 * @param row
	 * @param COLUMN_COUNT
	 */
	private void modifyDataFormatForExcel(
			Map<String, List<StudentInfo>> excelData, Row row, final int COLUMN_COUNT) {
		CommonColumns commonColumnsForPresent = new CommonColumns();
		CommonColumns commonColumnsForAbsent = new CommonColumns();
		Map<String,Boolean> studentsToAttendanceMap = null;
		Map<String,String> studentsToRemarksMap = null;
		String dateRep = null;
		for(int i=1 ; i<COLUMN_COUNT ; i++) {
			
			Cell cell =  row.getCell(i, Row.CREATE_NULL_AS_BLANK);
			
			switch(ResponseSheetFields.getEnumByPos(i)) {
			case TEACHER_NAME:
				commonColumnsForPresent.setTeacher(cell.getStringCellValue());
				commonColumnsForAbsent.setTeacher(cell.getStringCellValue());
				break;
			case TOPICS_TAUGHT:
				commonColumnsForPresent.setTopics(cell.getStringCellValue());
				break;
			case HOMEWORK_GIVEN:
				commonColumnsForPresent.setHomeworkGiven(cell.getStringCellValue());
				break;
			case ACTIVITIES:
				commonColumnsForPresent.setActivitiesPerformed(cell.getStringCellValue());
				break;
			case PLANS_FOR_NEXT_DAY:
				commonColumnsForPresent.setPlansForNextday(cell.getStringCellValue());
				commonColumnsForAbsent.setPlansForNextday(cell.getStringCellValue());
				break;
			case STUDENTS_TAUGHT:
				studentsToAttendanceMap = getStudentsToAttendanceMapping(
						cell.getStringCellValue());
				break;
			case REMARKS:
				studentsToRemarksMap = getStudentsToRemarksMapping(
						cell.getStringCellValue());
				break;
			case DATE_OF_CLASS:
				DateTime date = new DateTime(cell.getDateCellValue().getTime());
				dateRep = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + (date.getYear()%100);
				break;
			case TIMESTAMP:
				break;
			}
			
		}
		
		if(!excelData.containsKey(dateRep)) {
			excelData.put(dateRep, new ArrayList<>());
		}
		
		for(String student : studentsToAttendanceMap.keySet()) {
			Boolean isPresent = studentsToAttendanceMap.get(student);
			String remarks = studentsToRemarksMap.get(student);
			if(isPresent)
				excelData.get(dateRep).add(new StudentInfo(student, isPresent, remarks, commonColumnsForPresent));
			else
				excelData.get(dateRep).add(new StudentInfo(student, isPresent, remarks, commonColumnsForAbsent));
		}
	}
	
	/**
	 * create mapping of a student to his attendance from student-attendance data.
	 * Eg: Jannat-A,Kiron-P to {Jannat:false,Kiron:true}
	 * @param cellValue
	 * @return
	 */
	private static Map<String, Boolean> getStudentsToAttendanceMapping(String cellValue) {
		Map<String, Boolean> map = new HashMap<>();
		String[] studentWithAttendanceList = cellValue.split(",");
		for(String studentWithAttendance : studentWithAttendanceList) {
			studentWithAttendance = studentWithAttendance.trim();
			String[] splitByHyphen = studentWithAttendance.split("-");
			map.put(splitByHyphen[0].trim(), splitByHyphen[1].trim().equalsIgnoreCase(Constants.PRESENT));
		}
		return map;
	}
	
	/**
	 * create mapping of a student to the remarks corresponding to him from remarks column.
	 * Eg: Pooja-doing good in class
	 *     Aman - needs more concentration
	 *     
	 *     to
	 *     
	 *     {Pooja:doing good in class,Aman:needs more concentration}
	 * @param cellValue
	 * @return
	 */
	private static Map<String,String> getStudentsToRemarksMapping(String cellValue) {
		Map<String, String> map = new HashMap<>();
		if(!"".equals(cellValue)) {
			String[] studentWithRemarksList = cellValue.split("\n");
			for(String studentWithRemarks : studentWithRemarksList) {
				studentWithRemarks = studentWithRemarks.trim();
				String[] splitByHyphen = studentWithRemarks.split("-");
				map.put(splitByHyphen[0].trim(), splitByHyphen[1].trim());
			}
		}
		
		return map;
	}

}
