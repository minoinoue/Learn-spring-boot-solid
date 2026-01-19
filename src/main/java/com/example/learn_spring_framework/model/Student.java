package com.example.learn_spring_framework.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/*@Entity - Model class that maps with/ stored in a database
 * 
 * Purpose: Define a JPA entity
 */
@Entity 
@Table(name = "students") //Name of table

public class Student {
	@Id //Define primary key
	@Column(name = "student_id") //Name of column of table
	private String studentId; 
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;
	
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}) //When you save Student -> save User
	@JoinColumn(name = "id", referencedColumnName = "id") //reference key in Student, column name id
	//id of Student will reference to id of user
	private User user;
	
	public Student() {
		/*Hibernate request constructor no arguments
		 * 
		 *	+ When ORM read data on database, it needs a constructor no arguments
		 *to set fields one by one 
		 *
		 *	+ Ensure that every provider like Hibernate or EclispeLink all consistent
		 *
		 *	+ Hibernate usually create proxy to lazy loading or optimize queries 
		 *and proxy can created class without arguments
		 *
		 * <Proxy is a subclass that's created by Hibernate while runtime to replace
		 * real entity until Hibernate needs upload real data from database
		 * 
		 *How's work: When you get student, you don't have real <Student>,
		 *Proxy just has ID so when you call getStudentID() or getFullName(),
		 *Proxy will be actived and push sql select, then replace the data on
		 *Proxy and return real entity.
		 */
	}
	
	public Student(String studentId, String fullName, User user) {
		this.studentId = studentId;
		this.fullName = fullName;
		this.user = user;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}