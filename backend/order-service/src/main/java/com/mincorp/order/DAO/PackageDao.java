package com.mincorp.order.DAO;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mincorp.order.repositories.PackageRepository;

import jakarta.transaction.Transactional;
@Repository
public class PackageDao {
	@Autowired
	PackageRepository repo;
	public com.mincorp.order.beans.Package getPackage(int id) {
		return repo.findById(id);
	}
	public com.mincorp.order.beans.Package createPackage(com.mincorp.order.beans.Package p) {
		return repo.save(p);
	}
	public List<com.mincorp.order.beans.Package> getPackageBySellerId(int sId){
		return repo.findBySellerId(sId);
	}
	public void updateStatus(int id,String status,Date date) {
		com.mincorp.order.beans.Package p = repo.getById(id);
		p.setStatus(status);
		if(date!=null) p.setDelivered(date);
		repo.save(p);
	}
	public void updatePaymentStatus(int id,String status) {
		com.mincorp.order.beans.Package p = repo.getById(id);
		p.setPaymentStatus(status);
		repo.save(p);
	}
	@Transactional
	public void removePackage(int id){
		repo.delete(repo.getById(id));
	}
}
