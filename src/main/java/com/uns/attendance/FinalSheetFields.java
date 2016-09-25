package com.uns.attendance;

public enum FinalSheetFields {
	
	STUDENT_NAME("Name of Kid",0),
	ATTENDANCE("Present",1),
	TEACHER_NAME("Teacher",2),
	TOPICS_TAUGHT("Topics taught",3),
	HOMEWORK_GIVEN("Homework given",4),
	PLANS_FOR_NEXT_DAY("Plans for next day",5),
	REMARKS("Remarks",6),
	ACTIVITIES("Activites Performed",7);
	
	private String field;
	private int pos;
	
	private FinalSheetFields(String field, int pos) {
		this.field = field;
		this.pos = pos;
	}
	
	public String getField() {
		return field;
	}
	
	public int getPos() {
		return pos;
	}
	
	public static FinalSheetFields getEnumByPos(int pos) {
		for(FinalSheetFields field : FinalSheetFields.values()) {
			if(field.pos == pos) {
				return field;
			}
		}
		return null;
	}

}
