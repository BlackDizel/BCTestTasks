package ru.byters.contactlist;

/**contact info model. contains id, name, second name and phone**/
public class ProfileInfo {

	private String id;
	private String name;
	private String secondName;
	private String phone;
	
	public void setName(String value){
		this.name=value;
	}
	
	public void setSecondName(String value){
		this.secondName=value;
	}
	
	public void setPhone(String value){
		this.phone=value;
	}
	
	public void setID(String value){
		this.id=value;
	}
	
	public String getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSecondName(){
		return secondName;
	}
	
	public String getPhone(){
		return phone;
	}
	
}
