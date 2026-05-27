package com.app.earn.pojo;

import java.io.Serializable;

public class PaymentOption implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String label;        // UPI, PayPal
    private String value;        // upi, paypal
    private String country;

    private String inputLabel;
    private String placeholder;

    private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInputLabel() {
		return inputLabel;
	}

	public void setInputLabel(String inputLabel) {
		this.inputLabel = inputLabel;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public PaymentOption() {
		super();
		// TODO Auto-generated constructor stub
	}

    // getters and setters
}
