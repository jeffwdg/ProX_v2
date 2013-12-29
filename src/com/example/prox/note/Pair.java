package com.example.prox.note;

public class Pair {
	String id, desc;
	
	public Pair(String id, String desc)
	{	this.id=id;
		this.desc=desc;
	
	}
	public String getId()
	{
		return this.id;
	}
	public String getDesc()
	{
		return this.desc;
	}
}