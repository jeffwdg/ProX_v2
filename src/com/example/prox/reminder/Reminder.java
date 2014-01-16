package com.example.prox.reminder;

public class Reminder {
	
	private int id;
	private String title;
	private String date;
	private String time;
	private String description;
	
	public int getid(){
		return id;		
	}
	
	public int setid(int id){
		return this.id = id;
	}

	public String getTitle(){
		return title;
	}
	
	public String setTitle(String title){
		return this.title = title;
	}
	
	public String getDate(){
		return date;
	}
	
	public String setDate(String date){
		return this.date = date;
	}
	
	public String getTime(){
		return time;
	}
	
	public String setTime(String time){
		return this.time = time;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String setDescription(String description){
		return this.description = description;
	}
}
