package com.demo.uds.emartcatalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.demo.uds.emartcatalog.entity.Product;
import com.demo.uds.emartcatalog.repo.ProductRepository;

/*
CatalogService :
	Service responsible for providing various function to be used to fetch
	Products from Database
*/

@Service
public class CatalogService {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	public CatalogService(ProductRepository repository) {
		this.productRepo = repository;
	}
	
	public List<Product> findAll(){
		return Streamable.of(productRepo.findAll()).toList();
	}
	
	public Product findByName(String name){
		return productRepo.findByProductName(name);
	}
	
	public List<Product> findByType(String type){
		return productRepo.findByProductType(type);
	}
}
