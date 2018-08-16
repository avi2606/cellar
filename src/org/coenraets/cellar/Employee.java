package org.coenraets.cellar;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {

    private int empID;

    private String empName;

    private String empDept;

    private String empSalary;

   
	public int getId() {
		return empID;
	}

	public void setId(int id) {
		this.empID = id;
	}

	public String getName() {
		return empName;
	}

	public void setName(String name) {
		this.empName = name;
	}

	public String getDept() {
		return empDept;
	}

	public void setDept(String dept) {
		this.empDept = dept;
	}

	public String getSalary() {
		return empSalary;
	}

	public void setSalary(String salary) {
		this.empSalary = salary;
	}

	

}
