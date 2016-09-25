package com.uns.attendance;

public enum ResponseSheetFields {
	
	TIMESTAMP("Timestamp",0),
	DATE_OF_CLASS("Date of class",1),
	TEACHER_NAME("Teacher's name",2),
	STUDENTS_TAUGHT("Students taught",3),
	TOPICS_TAUGHT("Topics taught",4),
	HOMEWORK_GIVEN("Homework given",5),
	ACTIVITIES("Activites Performed",6),
	PLANS_FOR_NEXT_DAY("Plans for next day",7),
	REMARKS("Remarks",8);
	
	private String field;
	private int pos;
	
	private ResponseSheetFields(String field, int pos) {
		this.field = field;
		this.pos = pos;
	}
	
	public String getField() {
		return field;
	}
	
	public int getPos() {
		return pos;
	}
	
	public static ResponseSheetFields getEnumByPos(int pos) {
		for(ResponseSheetFields field : ResponseSheetFields.values()) {
			if(field.pos == pos) {
				return field;
			}
		}
		return null;
	}

}
