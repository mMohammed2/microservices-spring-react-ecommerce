package com.mincorp.product.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.product.beans.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findTop5ByOrderByTotalSoldDesc();
	List<Product> findBySellerId(int id);
	Product findByIdAndSellerId(int id,int sellerId);
}
