package com.example.graphql.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.graphql.entity.Blog;

public interface BlogRepository extends CrudRepository<Blog, Integer>{

	List<Blog> findByAuthor(String author);

	Blog findByTitle(String title);
}
