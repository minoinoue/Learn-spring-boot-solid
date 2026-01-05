package com.example.learn_spring_framework.dto.response;

/*Use for return DTO that don't take the password and username to client
 */

public class StudentInfoResponse {
	
	private String studentId;
    private String fullName;
    
	public StudentInfoResponse(String studentId, String fullName) {
		this.studentId = studentId;
		this.fullName = fullName;
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
}
