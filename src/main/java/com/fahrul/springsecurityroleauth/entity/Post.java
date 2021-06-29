package com.fahrul.springsecurityroleauth.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "POST")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {

	@Id
	@GeneratedValue
	private int postId;

	private String subject;

	private String description;

	private String userName;
	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;

}
