package com.demo.uds.emartcatalog.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/*
  Orders : 
  	1) Populated from products in cart while order is submitted
  	2) No mapped table as that is handled in Order-Service
 */

@Data
public class Orders implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7429896478650119860L;

	private String orderid;
	
	private String userid;
	
    private Integer productid;
	
    private Integer quantity;
	
    private Double amount;

    private Date orderdate;

    private Date deliverydate;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public Date getDeliverydate() {
		return deliverydate;
	}

	public void setDeliverydate(Date deliverydate) {
		this.deliverydate = deliverydate;
	}
}