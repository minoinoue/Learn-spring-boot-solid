package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

		@NotBlank(message = "Tên đăng nhập không được để trống.")
		private String userName;
		
		@NotBlank(message = "Password không được để trống!")
		@Size(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự.")
		private String password;

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
}
