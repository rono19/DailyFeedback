package com.uns.attendance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileLoader {
	
	/**
	 * loads all properties files as key-value pair
	 * @throws IOException
	 */
	public static Properties loadAllPropertiesFile() throws IOException {
		Properties properties = new Properties();
		Properties prop = new Properties();
		
		//load all properties file here
		loadProperties(Constants.FILE_PROPERTIES, properties);
		prop.putAll(properties);
		
		return prop;
	}
	
	private static void loadProperties(String fileName, Properties properties) throws IOException {
		InputStream inputStream = PropertyFileLoader.class.getClassLoader().getResourceAsStream(fileName);
		if(inputStream != null) {
			properties.load(inputStream);
		} else {
			throw new FileNotFoundException("File "+fileName+" not found.");
		}
	}

}
