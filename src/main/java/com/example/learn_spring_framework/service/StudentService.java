package com.example.learn_spring_framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.iStudentReadable;
import com.example.learn_spring_framework.repository.iStudentWriteable;

@Service
public class StudentService {
	
	private final iStudentReadable reader; 
	/*
	 * giữ tính kết nối giữa service và repository vì spring
	 * tự inject dpdc vào obj
	 * 
	 * chỉ được gán 1 lần trong hàm tạo
	 */
	private final iStudentWriteable writer;
	
	@Autowired
	public StudentService(iStudentReadable reader, iStudentWriteable writer){
		this.reader = reader;
		this.writer = writer;
	}
	
	public List<Student> getAllStudent(){
		return reader.findAllStudent();
	}
	
	public Student getfindByMsv(String msv) {
		if(msv == null || msv.trim().isEmpty()) 
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(reader.findByMsv(msv) == null)
			throw new IllegalStateException("Không tìm thấy sinh viên với mã sinh viên " + msv);
		return reader.findByMsv(msv);
	}
	
	public void modify(String msv, String fullname) {
		if(msv == null || msv.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(fullname == null || fullname.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");		
		writer.modify(msv, fullname);
	}
	
	public void add(String msv, String fullname) {
		if(msv == null || msv.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(fullname == null || fullname.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");
		if(reader.findByMsv(msv) != null)
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		writer.add(new Student(msv, fullname));
	}
}
