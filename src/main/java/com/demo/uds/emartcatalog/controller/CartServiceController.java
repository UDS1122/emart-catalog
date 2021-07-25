package com.demo.uds.emartcatalog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.uds.emartcatalog.cache.CartCache;
import com.demo.uds.emartcatalog.entity.Cart;
import com.demo.uds.emartcatalog.service.CartService;

/*
CartServiceController : 
 - Controller responsible for following :
 	1) Return products in Cart
 	2) Add product in Cart
 	3) Update product in Cart
 	4) Remove product from Cart
 	5) Empty Cart
 	
 	It will add the products in the cache and return from cache else from DB
 	At the same time it will keep adding/updating/removing products in DB so that User can check later on
*/

@RestController
public class CartServiceController {

	@Autowired
	CartService cartService;
	
	private final Map<String, List<Cart>> cache = CartCache.getCartCache();
	
	private final String userid = "user101";//in real scenario this would be coming from user session after authentication
	
	@GetMapping("/cart")
	@Produces("application/json")
	public ResponseEntity<?> getAllProductsInCart() {
		try {
			//also first try and check if it is in cache then return from cache else go to DB and fetch if there is anything
			List<Cart> cartProducts = cache.get(userid);
			if(cartProducts == null || cartProducts.isEmpty()) {
				cartProducts = cartService.findByUserId(userid);
				cache.put(userid, cartProducts);
			}
			return (new ResponseEntity<List<Cart>>(cartProducts, HttpStatus.OK));
		}catch(Exception exp) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Products not found in Cart", exp);
		}
	}
	
	
	  @PostMapping("/cart/add/{productid}/{qty}")
	  @Produces("application/json") 
	  public ResponseEntity<?> addProductInCartName(@PathVariable Optional<Integer> productid, @PathVariable Optional<Integer> qty) {
		  try {
			  final Cart newCartProduct = new Cart();
			  newCartProduct.setUserid(userid);
			  newCartProduct.setProductid(productid.get());
			  newCartProduct.setQuantity(qty.get());
			  addInCartCache(newCartProduct);
			  return (new ResponseEntity<String>(cartService.addInCart(newCartProduct), HttpStatus.CREATED));
		  }catch(Exception exp) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Product not added in cart", exp);
		  }
	  }
	  
	  
	  @PutMapping("/cart/update/{cartid}/{qty}")
	  @Produces("application/json")
	  public ResponseEntity<?> updateCart(@PathVariable Optional<Integer> cartid, @PathVariable Optional<Integer> qty) {
		  try {
			  String result = "";
			  
			  if(cache.isEmpty()) {
				  result = "Cart is empty";
				  return (new ResponseEntity<String>(result, HttpStatus.OK));
			  }
			  
			  //in real life we might not really have this case
			  Optional<Cart> cart = cache.get(userid).stream().filter(c->(c.getId() == cartid.get())).findAny();
			  if(cart.isPresent()) {
				  final Cart updatedCart = cart.get();
				  updatedCart.setQuantity(qty.get());
				  updatedCart.setPrice(qty.get() * updatedCart.getProduct().getPrice());
				  result = cartService.updateItemInCart(updatedCart);
			  }
					  
			  return (new ResponseEntity<String>(result, HttpStatus.ACCEPTED));
		  }catch(Exception exp) {
				throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Cart not updated", exp);
		  }
	  }
	  
	  @DeleteMapping("/cart/remove/{cartid}")
	  public ResponseEntity<?> removeFromCart(@PathVariable Optional<Integer> cartid) {
		  try {
			  String result = "";
			  
			  if(cache.isEmpty()) {
				  result = "Cart is empty";
				  return (new ResponseEntity<String>(result, HttpStatus.OK));
			  }
			  
			  //in real life we might not really have this case
			  if(cache.get(userid).stream().anyMatch(c->(c.getId() == cartid.get()))) {
				  removeFromCartCache(cartid.get());
				  result = cartService.removeItemFromCart(cartid.get());
			  }
					  
			  return (new ResponseEntity<String>(result, HttpStatus.OK));
		  }catch(Exception exp) {
				throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Item not removed from Cart", exp);
		  }
	  }
	  
	  @DeleteMapping("/cart/empty")
	  public ResponseEntity<?> removeAllFromCart() {
		  try {
			  String result = "Cart is empty";
			  if(!cache.isEmpty()) {
				  cache.get(userid).forEach(c->{
					  cartService.removeItemFromCart(c.getId());
				  });
				  removeFromCartCache(null);
				  result = "Items removed. Cart is empty";
			  }
			  return (new ResponseEntity<String>(result, HttpStatus.OK));
		  }catch(Exception exp) {
				throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "No items removed from Cart", exp);
			}
	  }
	  
	  private void addInCartCache(final Cart cart) {
		  List<Cart> products = cache.get(userid);
		  if(products == null) {
			products = new ArrayList<>();
		  }
		  products.add(cart);
		  cache.put(userid, products);
	  }
	  
	  private void removeFromCartCache(final Integer cartid) {
		  if(cartid == null) {
			cache.remove(userid);//all items removed from cache for the particular user
		  }else {
			cache.get(userid).removeIf(c -> (c.getId() == cartid));//remove a particular one
		  }
	  }
}