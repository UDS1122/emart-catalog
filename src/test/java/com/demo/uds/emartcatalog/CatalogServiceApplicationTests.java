package com.demo.uds.emartcatalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.uds.emartcatalog.controller.CatalogServiceController;
import com.demo.uds.emartcatalog.entity.Product;
import com.demo.uds.emartcatalog.repo.ProductRepository;
import com.demo.uds.emartcatalog.service.CatalogService;

@SpringBootTest
class CatalogServiceApplicationTests {

	@InjectMocks
	private CatalogServiceController catalogController = new CatalogServiceController();

	@Mock
	private ProductRepository repo;
	
	@Mock
	private CatalogService catalogService = new CatalogService(repo);
	
	@BeforeAll
	public static void init(){
		CatalogServiceApplicationTests testClass = new CatalogServiceApplicationTests();
		MockitoAnnotations.openMocks(testClass);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAllProducts() {
		when(catalogService.findAll()).thenReturn(getProductsFromStubDB());
		ResponseEntity<?> result =  catalogController.getAllProducts();
		assertThat(result.getStatusCode().equals(HttpStatus.OK));
		assertThat(((List<Product>)result.getBody()).size()).isGreaterThan(0);
	}
	
	@Test
	public void getProductsByName() {
		when(catalogService.findByName("iPhone 8")).thenReturn(getProductsFromStubDB().stream().filter(p->p.getName().equals("iPhone 8")).findAny().get());
		ResponseEntity<?> result =  catalogController.getProductByName(Optional.of("iPhone 8"));
		assertThat(result.getStatusCode().equals(HttpStatus.OK));
		assertThat(((Product)result.getBody()).getName().equals("iPhone 8")).isTrue();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getProductsByType() {
		when(catalogService.findByType("Laptop")).thenReturn(getProductsFromStubDB().stream().filter(p->p.getType().equals("Laptop")).toList());
		ResponseEntity<?> result =  catalogController.getProductByType(Optional.of("Laptop"));
		assertThat(result.getStatusCode().equals(HttpStatus.OK));
		assertThat(((List<Product>)result.getBody()).size()).isEqualTo(1);
	}
	
	private List<Product> getProductsFromStubDB(){
		List<Product> products = new ArrayList<>();
		Product mobile = new Product();
		mobile.setId(1); 
		mobile.setMake("Apple");
		mobile.setModel("iPhone"); 
		mobile.setName("iPhone 8");
		mobile.setPrice(700.99); 
		mobile.setAvailable(true); 
		mobile.setType("Mobile");
		//laptop 
		Product laptop = new Product(); 
		laptop.setId(2);
		laptop.setMake("Dell"); 
		laptop.setModel("Dell 2021 v1");
		laptop.setName("Nitro"); 
		laptop.setPrice(1200.99); 
		laptop.setAvailable(true);
		laptop.setType("Laptop");
		
		products.add(mobile); 
		products.add(laptop);
		
		return products;
	}
}