package com.mincorp.cache.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mincorp.cache.DTO.CachedProduct;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/products/save")
    public String saveProduct(@RequestBody List<CachedProduct> product) {
        redisTemplate.opsForValue().set("allProducts", product, 10, TimeUnit.MINUTES);
        return "Product saved to cache!";
    }

    @GetMapping("/products/get")
    public List<CachedProduct> getProduct() {
        return (List<CachedProduct>) redisTemplate.opsForValue().get("allProducts");
    }
}