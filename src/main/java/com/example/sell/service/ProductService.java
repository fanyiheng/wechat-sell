package com.example.sell.service;

import com.example.sell.dto.CarDto;
import com.example.sell.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product findOne(String productId);

    List<Product> findUpAll();

    Page<Product> findAll(Pageable pageable);

    Product save(Product product);

    void increaseStock(List<CarDto> carDtos);

    void decreaseStock(List<CarDto> carDtos);

    Product onSale(String productId);

    Product offSale(String productId);
}
