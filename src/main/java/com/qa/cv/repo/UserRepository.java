package com.qa.cv.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.qa.cv.model.Person;
import com.qa.cv.model.User;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends MongoRepository<User, String> {

	List<User> findByRole(@Param("role") String role);
	
	User findByuserName (String dame);

}
