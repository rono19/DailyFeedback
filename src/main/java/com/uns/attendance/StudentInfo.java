package com.uns.attendance;

public class StudentInfo {
	
	private String name;
	
	private boolean isPresent;
	
	private String remarks;
	
	private CommonColumns commonColumns;
	
	public StudentInfo(String name, boolean isPresent, String remarks) {
		this.name = name;
		this.isPresent = isPresent;
		this.remarks = remarks;
	}
	
	public StudentInfo(String name, boolean isPresent, String remarks, CommonColumns commonColumns) {
		this(name,isPresent,remarks);
		this.commonColumns = commonColumns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public CommonColumns getCommonColumns() {
		return commonColumns;
	}

	public void setCommonColumns(CommonColumns commonColumns) {
		this.commonColumns = commonColumns;
	}

}
