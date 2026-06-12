package com.mincorp.seller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.seller.beans.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
}
