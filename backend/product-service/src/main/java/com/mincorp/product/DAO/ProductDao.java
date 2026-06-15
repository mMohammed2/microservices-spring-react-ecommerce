package com.mincorp.product.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.product.beans.Product;
import com.mincorp.product.repositories.ProductRepository;

@Repository
public class ProductDao {
	@Autowired
	ProductRepository repo;
	public List<Product> getBestProducts(){
		return repo.findTop5ByOrderByTotalSoldDesc();
	}
	public Product addProduct(Product p) {
		return repo.save(p);
	}
	public List<Product> getProductsForSeller(int id){
		return repo.findBySellerId(id);
	}
	@SuppressWarnings("deprecation")
	public Product findById(int id) 
	{
		return repo.getById(id);
	}
	
	public List<Product> getProductByIds(List<Integer> ids){
		return repo.findAllById(ids);
	}
	public void update(Product p) {
		repo.save(p);
	}
	public Product getSellerProduct(int id,int sellerId) {
		return repo.findByIdAndSellerId(id, sellerId);
	}
	public Product getProduct(int id) {
		return repo.getById(id);
	}
	public List<Product> getAllProducts(){
		return repo.findAll();
	}
	public List<Product> getSearchedProducts(String query){
		return repo.findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(query, query);
	}
}
