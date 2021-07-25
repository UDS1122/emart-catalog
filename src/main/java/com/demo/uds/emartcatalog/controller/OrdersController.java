package com.demo.uds.emartcatalog.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.demo.uds.emartcatalog.cache.CartCache;
import com.demo.uds.emartcatalog.entity.Cart;
import com.demo.uds.emartcatalog.entity.Orders;
import com.demo.uds.emartcatalog.service.CartService;

/*
OrdersController : 
 - Controller responsible for following :
 	1) Submit order of products from Cart
 	2) Make a synchronous call to Order-Service and pass the list of the orders 
 	3) Remove products from the cache (as the order is placed)
 	4) Remove products from the DB (as the order is placed)
*/

@RestController
public class OrdersController {

	@Autowired
	CartService cartService;
	
	@Autowired
	RestTemplate restTemplate;
	
	private final Map<String, List<Cart>> cache = CartCache.getCartCache();
	
	final String user = "user101";
	
	@PostMapping("/orders/submit")
	@Produces("application/json")
	public ResponseEntity<?> submitOrder() {
		try {
			//in real life we will check in session and if valid then take out the user and submit order
			final List<Cart> carts = cache.get(user);
			
			if(carts == null || carts.isEmpty()) {
				return (new ResponseEntity<String>("Cart is empty. Order cannot be placed", HttpStatus.OK));
			}
			final String orderId = UUID.randomUUID().toString();
			final List<Orders> orders = new ArrayList<>();
			carts.forEach(o->{
				final Orders order = new Orders();
				order.setOrderid(orderId);
				order.setUserid(user);
				order.setProductid(o.getProductid());
				order.setQuantity(o.getQuantity());
				order.setAmount(o.getPrice());
				order.setOrderdate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
				order.setDeliverydate(Date.valueOf(LocalDate.now(ZoneId.systemDefault()).plusDays(2)));
				//add in the list
				orders.add(order);
				cartService.removeItemFromCart(o.getId());//remove from the db as well...as orders will be placed...surely should have been in different place
			});
			
			final String result = restTemplate.postForObject("http://order-service/orders/execute", orders, String.class);
			
			cache.remove(user);//order has been placed so remove from the cache
			
			return (new ResponseEntity<String>(result, HttpStatus.CREATED));
		}catch(Exception exp) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Orders submission failed", exp);
		}
	}
}