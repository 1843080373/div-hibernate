package com.orm.model;

import com.orm.core.enums.Column;
import com.orm.core.enums.Entity;
import com.orm.core.enums.Id;
import com.orm.core.enums.Table;

@Entity
@Table(name="t_user")
public class User {
	@Column(name="password",db_type="VARCHAR")
	private String password;
	@Id(name="user_id",db_type="int",generator="uuid")
	private Integer userId;
	@Column(name="phone",db_type="VARCHAR")
	private String phone;
	@Column(name="user_name",db_type="VARCHAR")
	private String userName;

	public String getPassword() {
		return password;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getPhone() {
		return phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [password=" + password + ", userId=" + userId + ", phone=" + phone + ", userName=" + userName
				+ "]";
	}

}
