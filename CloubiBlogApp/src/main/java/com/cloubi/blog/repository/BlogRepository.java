package com.cloubi.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cloubi.blog.model.Blog;

public interface BlogRepository extends MongoRepository<Blog, String> {

}
