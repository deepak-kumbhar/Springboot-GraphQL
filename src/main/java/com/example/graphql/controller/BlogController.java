package com.example.graphql.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.graphql.dao.BlogRepository;
import com.example.graphql.entity.Blog;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * @author Mindbowser | deepak.kumbhar@mindbowser.com
 * @Created Date: Jan 30, 2021 4:38:36 PM
 *
 */
@RestController
public class BlogController {

	@Autowired
	private BlogRepository blogRepository;

	@Value("classpath:blog.graphqls")
	private Resource schemaResource;

	private GraphQL graphQL;

	/**
	 * Load schema will loaded when the application will get run
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void loadSchema() throws IOException {
		File schemaFile = schemaResource.getFile();
		TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	/**
	 * Build wiring and configure the queries in it with the methods.
	 * 
	 * @return
	 */
	private RuntimeWiring buildWiring() {
		DataFetcher<List<Blog>> fetcher = data -> (List<Blog>) blogRepository.findAll();

		DataFetcher<List<Blog>> fetcher1 = data -> blogRepository.findByAuthor(data.getArgument("author"));

		DataFetcher<Blog> fetcher2 = data -> (blogRepository.findByTitle(data.getArgument("title")));

		return RuntimeWiring.newRuntimeWiring()
				.type("Query",
						typeWriting -> typeWriting.dataFetcher("getAllBlog", fetcher)
								.dataFetcher("findBlogByAuthor", fetcher1).dataFetcher("findBlogByTitle", fetcher2))
				.build();
	}

	/**
	 * Save blog details in H2 database.
	 * 
	 * @param blogs
	 * @return
	 */
	@PostMapping("/save")
	public String addBlog(@RequestBody List<Blog> blogs) {
		blogRepository.saveAll(blogs);
		return "Blogs added " + blogs.size();
	}

	/**
	 * Get blog list from H2 database
	 * 
	 * @return
	 */
	@GetMapping
	public List<Blog> getBlogList() {
		return (List<Blog>) blogRepository.findAll();
	}

	/**
	 * get blog list - graphQL integration
	 * 
	 * @param query
	 * @return
	 */
	@PostMapping("/getAllBlog")
	public ResponseEntity<Object> getAllBlog(@RequestBody String getAllQuery) {
		ExecutionResult result = graphQL.execute(getAllQuery);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	/**
	 * Get blog list by Author - GraphQL integration
	 * 
	 * @param query
	 * @return
	 */
	@PostMapping("/getBlogByAuthor")
	public ResponseEntity<Object> getBlogByAuthor(@RequestBody String getByAuthorQuery) {
		ExecutionResult result = graphQL.execute(getByAuthorQuery);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	/**
	 * Get blog by title - GraphQL method
	 * 
	 * @param query
	 * @return
	 */
	@PostMapping("/getBlogByTitle")
	public ResponseEntity<Object> getSingleBlogByTitle(@RequestBody String getByTitleQuery) {
		ExecutionResult result = graphQL.execute(getByTitleQuery);
		return new ResponseEntity<>(result, HttpStatus.OK);

	}
}
