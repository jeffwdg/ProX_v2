package com.example.prox;

import android.content.Context;

public class Ebook {
	
	private String id;
	private String title;
    private String author;
    private String ISBN;
    private String filename;
    private String cover;
    private String category;
    private String status; 
  
 
    
    public String getID() {
        return id;
    }
 
    public String setID(String id) {
        return this.id = id;
    }
    
    
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getAuthor() {
        return author;
    }
 
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getISBN() {
        return ISBN;
    }
 
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    public String getCover() {
        return cover;
    }
 
    public void setCover(String cover) {
        this.cover= cover;
    }
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status= status;
    }
    
    public String getCategory() {
        return category;
    }
 
    public void setCategory(String categgory) {
        this.category= category;
    }
    
    
    public String getFilename() {
        return filename;
    }
 
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
	public CharSequence getText() {
		// TODO Auto-generated method stub
		return null;
	}
	

 
 
 
}
