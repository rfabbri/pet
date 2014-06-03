package com.pulapirata.core;

public class Aviso{
	private String aviso; 	
	
	public Aviso(){
	}
	
	public Aviso(String aviso){
		this.aviso = aviso;
	}
	
	public String getAviso(){
		return this.aviso;
	}
	
	public void setAviso(String aviso){
		this.aviso = aviso;
	}

	public void remove(){
		this.aviso = null;
	}

	public boolean isEmpty(){
		if(aviso==null){
			return true;
		}else{
			return false;
		}
	}
}
