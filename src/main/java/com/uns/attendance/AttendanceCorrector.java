package com.uns.attendance;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AttendanceCorrector {
	
	/**
	 * main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Properties prop = PropertyFileLoader.loadAllPropertiesFile();
		ExcelReader excelReader = ExcelReader.getInstance();
		excelReader.setProp(prop);
		ExcelWriter excelWriter = ExcelWriter.getInstance();
		excelWriter.setProp(prop);
		Map<String, List<StudentInfo>> data = excelReader.readResponseExcelAndCreateData();
		excelWriter.writeDataIntoFinalExcel(data);
    }
	
}
