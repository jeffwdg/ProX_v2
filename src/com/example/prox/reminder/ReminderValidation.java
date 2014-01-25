package com.example.prox.reminder;

import java.util.regex.Pattern;

import android.widget.EditText;

public class ReminderValidation {
	 // Regular Expression
    // you can change the expression based on your need
    private static final String TITLE_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String TITLE_MSG = "Title too long.";
   
   

    // call this method when you need to check email validation
    public static boolean TitleValid(EditText editText, boolean required) {
        return isValid(editText, TITLE_REGEX,TITLE_MSG, required);
    }

    
    public static boolean DateValid(EditText editText, boolean required) {
    
    	String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        if ( required && hasText(editText) ) 
        	return false;

        return true;
    }
    
    public static boolean TimeValid(EditText editText, boolean required) {
        
    	String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        if ( required && hasText(editText) ) 
        	return false;

        return true;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && hasText(editText) ) 
        	return false;
        
        if ( required && validLength(editText) ) 
        	return false;

        // pattern doesn't match so returning false
//        if (required && !Pattern.matches(regex, text)) {
//            editText.setError(errMsg);
//            return false;
//        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0 ) {
            editText.setError(REQUIRED_MSG);
            return true;
        }
       
      
        return false;
    }
    
    public static boolean validLength(EditText editText) {

        String text = editText.getText().toString();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() > 35 ) {
            editText.setError(TITLE_MSG);
            return true;
        }
       
      
        return false;
    }

}
