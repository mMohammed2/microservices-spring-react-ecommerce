package com.mincorp.pay.controllers.DTO;

public class CardRequest {
	float amount;
    long cardNumber;
    String expiryDate;
    int cvv;
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public CardRequest(float amount, long cardNumber, String expiryDate, int cvv) {
		super();
		this.amount = amount;
		this.cardNumber = cardNumber;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
	}
	public CardRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
