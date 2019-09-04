package com.brainz.framework.sample.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userInfo")
public class User implements Serializable {
 
	private static final long serialVersionUID = -2343243243242432341L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "userId")
	private String userId;
 
	@Column(name = "userName")
	private String userName;
 
	protected User() {
	}
 
	public User(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}
 
	@Override
	public String toString() {
		return String.format("User Info [id=%d, userId='%s', userName='%s'] \n", id, userId, userName);
	}
	public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}