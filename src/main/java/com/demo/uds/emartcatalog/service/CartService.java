package com.demo.uds.emartcatalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.demo.uds.emartcatalog.entity.Cart;
import com.demo.uds.emartcatalog.entity.Product;
import com.demo.uds.emartcatalog.repo.CartRepository;
import com.demo.uds.emartcatalog.repo.ProductRepository;

/*
 CartService :
 	Service responsible for providing various function to be used to add/update/delete
 	from Cart in Database
 */

@Service
public class CartService {

	private CartRepository cartRepo;
	private ProductRepository productRepo;
	
	@Autowired
	public CartService(CartRepository repository, ProductRepository productRepo) {
		this.cartRepo = repository;
		this.productRepo = productRepo;
	}
	
	public List<Cart> findAll(){
		return Streamable.of(cartRepo.findAll()).toList();
	}
	
	public List<Cart> findByUserId(String userid){
		return cartRepo.findByUserId(userid);
	}
	
	public String addInCart(Cart cart){
		final Product product = productRepo.findById(cart.getProductid()).get();
		cart.setProduct(product);
		cart.setPrice(cart.getQuantity() * product.getPrice());
		final Cart added = cartRepo.save(cart);
		return added == null ? "Product not added" : "Product added in Cart";
	}
	
	public String removeItemFromCart(Integer cartid){
		cartRepo.deleteById(cartid);
		return "Item removed from Cart";
	}
	
	public String updateItemInCart(Cart cart){
		cartRepo.save(cart);
		return "Item updated in Cart";
	}
}