package com.modbusparser.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "register")
public class Register {
	
	@Id
	private int id; 
	private int reg21; 
	private int reg22;
	private int reg92;
	
	public Register() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReg21() {
		return reg21;
	}
	public void setReg21(int reg21) {
		this.reg21 = reg21;
	}
	public int getReg22() {
		return reg22;
	}
	public void setReg22(int reg22) {
		this.reg22 = reg22;
	}
	public int getReg92() {
		return reg92;
	}
	public void setReg92(int reg92) {
		this.reg92 = reg92;
	}
	
}