package com.demo.uds.emartcatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 * Product :
 * 	1) Entity mapped with Product table in database
 * 	2) Has details about each product e.g. Name, Make, Price etc.
 */

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

	@Id
	@GeneratedValue
	@JsonProperty
	private Integer id;
	
	@Column
	@JsonProperty
	private String name;
	
	@Column
	@JsonProperty
	private String type;
	
	@Column
	@JsonProperty
	private String make;
	
	@Column
	@JsonProperty
	private String model;
	
	@Column
	@JsonProperty
	private double price;
	
	@Column
	@JsonProperty
	private boolean available;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}