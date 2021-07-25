package com.demo.uds.emartcatalog.controller;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.uds.emartcatalog.entity.Product;
import com.demo.uds.emartcatalog.service.CatalogService;

/*
CatalogServiceController : 
 - Controller responsible for following :
 	1) Return all products in DB (real life..pagination will be there)
 	2) Return product by Name
 	3) Return product by Type
*/

@RestController
public class CatalogServiceController {

	@Autowired
	CatalogService catalogService;
	
	@GetMapping("/catalog")
	@Produces("application/json")
	public ResponseEntity<?> getAllProducts() {
		try {
			return (new ResponseEntity<List<Product>>(catalogService.findAll(), HttpStatus.OK));
		}catch(Exception exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Products not found", exp);
		}
	}
	
	@GetMapping("/catalog/name/{name}")
	@Produces("application/json")
	public ResponseEntity<?> getProductByName(@PathVariable Optional<String> name) {
		try {
			return (new ResponseEntity<Product>(catalogService.findByName(name.get()), HttpStatus.OK));
		}catch(Exception exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", exp);
		}
	}
	
	@GetMapping("/catalog/type/{type}")
	@Produces("application/json")
	public ResponseEntity<?> getProductByType(@PathVariable Optional<String> type) {
		try {
			return (new ResponseEntity<List<Product>>(catalogService.findByType(type.get()), HttpStatus.OK));
		}catch(Exception exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found", exp);
		}
	}
}
