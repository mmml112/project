package com.entity;//它是数据库中 “用户表（users）” 的 Java 映射类。

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/** 
 * 用户
 */
@TableName("users")//告诉程序，这个类对应数据库中的 users 表。
public class UserEntity implements Serializable {//implements Serializable：允许这个类被序列化（方便保存、传输用户数据）。
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.AUTO)//@TableId：代表这个字段是主键（数据库中最重要的唯一编号）。
	private Long id;
	
	/**
	 * 用户账号
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 用户类型
	 */
	private String role;
	
	private Date addtime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
