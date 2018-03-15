package com.example.sell.repository;

import com.example.sell.entity.Product;
import com.example.sell.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String>{
    List<Product> findByProductStatus(ProductStatus productStatus);
}
