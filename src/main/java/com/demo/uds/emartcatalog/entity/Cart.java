package com.demo.uds.emartcatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 
 Cart : 
 	1) Entity mapped with table CART in database
 	2) It will have the PRODCUT that is added along with numbers of products and its overall price
 	3) Return as JSON from Catalog Controller
 */

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cart {

	@Id
	@GeneratedValue
	@JsonProperty
    private Integer id;
	
	@Column
	@JsonProperty
	private String userid;
	
	@Column
	@JsonProperty
    private Integer productid;
	
	@Column
	@JsonProperty
    private Integer quantity;
	
	@Column
	@JsonProperty
    private Double price;
	
	@ManyToOne
    @JoinColumn(name = "productid", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}