package com.demo.uds.emartcatalog.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.demo.uds.emartcatalog.entity.Cart;

/*
 	CartRepository : 
 		1) Repository for carrying out various actions on table Cart
 		2) Couple of extra functions apart from CrudRepository to handle specific cases
 */

public interface CartRepository extends CrudRepository<Cart, Integer> {
	
	@Query("select c from Cart c where c.userid = ?1")
	List<Cart> findByUserId(String userid);
	
	@Query("Delete from Cart c where c.userid = ?1")
	void removeByUserId(String userid);
	
}
