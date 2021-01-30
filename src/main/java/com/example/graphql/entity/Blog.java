package com.example.graphql.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Blog {
	
	@Id
	private Integer id;
	
	private String title;
	private String author;
	private String content;
	private String[] tags;
	

}
