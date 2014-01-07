package com.project.detail;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetail implements Serializable {
	private String ProductID;
	private String Group;
	private String Name;
	private String Brand;
	private String Kind;
	private String Photo;
	private String PhotoSmall;
	private String Price;
	private String Amount;
	private String Date;
	private Boolean Status ;
	public ProductDetail() {

	}

	public ProductDetail(String productID, String group, String name,
			String brand, String kind, String photo, String price,
			String amount, String date, String PhotoSmall) {
		this.ProductID = productID;
		this.Name = name;
		this.Photo = photo;
		this.Amount = amount;
		this.Group = group;
		this.Brand = brand;
		this.Kind = kind;
		this.Price = price;
		this.Date = date;
		this.PhotoSmall = PhotoSmall;
	}
	public Boolean getStatus() {
		return Status;
	}
	public void setStatus(Boolean status) {
		Status = status;
	}
	public String getPhotoSmall() {
		return PhotoSmall;
	}

	public void setPhotoSmall(String photoSmall) {
		PhotoSmall = photoSmall;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String group) {
		Group = group;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getKind() {
		return Kind;
	}

	public void setKind(String kind) {
		Kind = kind;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	

}
