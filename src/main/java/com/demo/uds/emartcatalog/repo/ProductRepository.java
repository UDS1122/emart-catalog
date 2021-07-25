package com.demo.uds.emartcatalog.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.demo.uds.emartcatalog.entity.Product;

/*
	ProductRepository : 
		1) Repository for carrying out various actions on table Product
		2) Couple of extra functions apart from CrudRepository to handle specific cases
*/

public interface ProductRepository extends CrudRepository<Product, Integer> {
	
	@Query("select p from Product p where p.name = ?1")
	Product findByProductName(String emailAddress);
	
	@Query("select p from Product p where p.type = ?1")
	List<Product> findByProductType(String emailAddress);
}
