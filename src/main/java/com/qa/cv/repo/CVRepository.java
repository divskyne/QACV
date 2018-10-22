package com.qa.cv.repo;

import java.awt.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.qa.cv.model.Cv;

@RepositoryRestResource(collectionResourceRel = "cv", path = "cv")
public interface CVRepository extends MongoRepository<Cv, String> {
	Cv findByCvid(@Param("files_id") String files_id);
}
