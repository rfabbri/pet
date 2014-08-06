package com.pulapirata.core;

public class Aviso{
	private String aviso; 	
  private int prioridade;
	
	public Aviso(){
    aviso = "";
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

	public void remove() {
		this.aviso = "";
	}

	public boolean isEmpty(){
		if(aviso != null && !aviso.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
}
