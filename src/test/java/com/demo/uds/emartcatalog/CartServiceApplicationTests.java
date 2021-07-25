package com.demo.uds.emartcatalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.uds.emartcatalog.cache.CartCache;
import com.demo.uds.emartcatalog.controller.CartServiceController;
import com.demo.uds.emartcatalog.entity.Cart;
import com.demo.uds.emartcatalog.entity.Product;
import com.demo.uds.emartcatalog.repo.CartRepository;
import com.demo.uds.emartcatalog.repo.ProductRepository;
import com.demo.uds.emartcatalog.service.CartService;

@SpringBootTest
class CartServiceApplicationTests {

	@InjectMocks
	private CartServiceController cartController = new CartServiceController();

	@Mock
	private CartRepository cartRepo;
	
	@Mock
	private ProductRepository productRepo;
	
	@Mock
	private CartService cartService = new CartService(cartRepo, productRepo);
	
	private String user = "user101";
	
	private Map<String, List<Cart>> CACHE = CartCache.getCartCache();
	
	@BeforeAll
	public static void init(){
		CartServiceApplicationTests testClass = new CartServiceApplicationTests();
		MockitoAnnotations.openMocks(testClass);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getCartDetails() {
		when(cartService.findByUserId(user)).thenReturn(getCartsFromStubDB());
		ResponseEntity<?> result = cartController.getAllProductsInCart();
		assertThat(result.getStatusCode().equals(HttpStatus.OK));
		assertThat(((List<Cart>)result.getBody()).size()).isGreaterThan(0);
	}
	
	@Test
	public void addProductInCart() {
		int oldSize = CACHE.get(user).size();
		Cart cart = new Cart();
		cart.setId(3);
		when(cartService.addInCart(cart)).thenReturn(addOneMoreFromStubDB(3, 1));
		ResponseEntity<?> result = cartController.addProductInCartName(Optional.of(3), Optional.of(1));
		assertThat(result.getStatusCode().equals(HttpStatus.CREATED));
		assertThat(CACHE.get(user).size() > oldSize).isTrue();//as one has been added
	}
	
	@Test
	public void removeProductInCart() {
		int oldSize = CACHE.get(user).size();
		when(cartService.removeItemFromCart(3)).thenReturn(removeOneFromStubDB(3));
		ResponseEntity<?> result = cartController.removeFromCart(Optional.of(3));
		assertThat(result.getStatusCode().equals(HttpStatus.OK));
		assertThat(oldSize > CACHE.get(user).size()).isTrue();//as one has been removed
	}
	
	@Test
	public void updateProductInCart() {
		Cart cartItem = CACHE.get(user).get(0);
		int oldQty = cartItem.getQuantity();
		int newSize = oldQty+1;
		when(cartService.updateItemInCart(cartItem)).thenReturn(updateCartFromStubDB(cartItem.getId(), newSize));
		ResponseEntity<?> result = cartController.updateCart(Optional.of(cartItem.getId()), Optional.of(newSize));
		assertThat(result.getStatusCode().equals(HttpStatus.ACCEPTED));
		assertThat(cartItem.getQuantity()).isEqualTo(newSize);
	}
	
	private List<Cart> getCartsFromStubDB(){
		List<Cart> cartList = new ArrayList<>();
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

		Cart cart1 = new Cart();
		cart1.setId(1);
		cart1.setProductid(mobile.getId());
		cart1.setQuantity(2);
		cart1.setPrice(cart1.getQuantity() * mobile.getPrice());
		cart1.setUserid(user);
		cart1.setProduct(mobile);
		
		Cart cart2 = new Cart();
		cart2.setId(2);
		cart2.setProductid(laptop.getId());
		cart2.setQuantity(1);
		cart2.setPrice(cart2.getQuantity() * laptop.getPrice());
		cart2.setUserid(user);
		cart2.setProduct(laptop);
		
		cartList.add(cart1);
		cartList.add(cart2);
		
		CACHE.put(user, cartList);
		
		return cartList;
	}
	
	private String addOneMoreFromStubDB(int id, int qty){
		Product mobile = new Product();
		mobile.setId(id); 
		mobile.setMake("Samung");
		mobile.setModel("Note"); 
		mobile.setName("Note 8");
		mobile.setPrice(810.99); 
		mobile.setAvailable(true); 
		mobile.setType("Mobile");
		
		Cart cart3 = new Cart();
		cart3.setId(id);
		cart3.setProductid(mobile.getId());
		cart3.setQuantity(qty);
		cart3.setPrice(cart3.getQuantity() * mobile.getPrice());
		cart3.setUserid(user);
		cart3.setProduct(mobile);
		CACHE.get(user).add(cart3);
		
		return "Product added in Cart";
	}
	
	private String removeOneFromStubDB(Integer cartid){
		CACHE.get(user).removeIf(c -> (c.getId() == cartid));
		return "Item removed from Cart";
	}
	
	private String updateCartFromStubDB(Integer cartid, Integer qty){
		Cart cart = CACHE.get(user).stream().filter(c->c.getId() == cartid).findAny().get();
		cart.setQuantity(qty);
		cart.setPrice(cart.getProduct().getPrice() * qty);
		return "Item updated in Cart";
	}
}