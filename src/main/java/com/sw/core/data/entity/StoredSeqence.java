package com.sw.core.data.entity;

import java.io.Serializable;


/**
 * 自增主键bean
 * @author Administrator
 *
 */
public class StoredSeqence implements Serializable{
	
	private static final long serialVersionUID = 1L;  
    
    private String collName;  
    
    private Long value;  

    public StoredSeqence(){  
    }  
    
    public StoredSeqence(String collName) {  
    	this.collName = collName;  
    }  

    public Long getValue() {  
    	return value;  

    }  

    public void setValue(Long value) {  
    	this.value = value;  
    }  

    public String getCollName() {  
    	return collName;  
    }  

    public void setCollName(String collName) {  
    	this.collName = collName;  
    } 
}
