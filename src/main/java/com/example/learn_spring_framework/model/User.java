package com.example.learn_spring_framework.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false, name = "user_name")
	private String userName;
	
	@Column(nullable = false, name = "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER) //when you get user, hibernate will upload roles beside.
	//Security need roles after sign in to authorize =>> EAGER
    @JoinTable( name = "user_roles", 
                joinColumns = @JoinColumn(name = "user_id"), 
                inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>(); //avoid null pointer
	
	//1-1 relationship
	@OneToOne(mappedBy = "user") //it means Student holds the reference key
	@JsonIgnore
	//Find the "user" variable in class Student
	private Student student;
	/*This student is used to trace back, because this relation ship is
	 *bidirectional so in Entity Student also has private User user
	 */
	public User() {
		
	}
	
	public User(String userName, String password, Set<Role> roles) {
		this.userName = userName;
		this.password = password;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	
}
