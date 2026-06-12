package com.mincorp.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mincorp.order.beans.Package;

public interface PackageRepository extends JpaRepository<com.mincorp.order.beans.Package, Integer> {
	public Package findById(int id);
	public List<Package> findBySellerId(int sellerId);
}
