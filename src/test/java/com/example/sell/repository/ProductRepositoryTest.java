package com.example.sell.repository;

import com.example.sell.entity.Product;
import com.example.sell.enums.ProductStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;
    @Test
    public void save(){
        Product product = new Product();
        product.setProductId("123456");
        product.setProductName("鸡蛋饼");
        product.setProductPrice(new BigDecimal("1.5"));
        product.setProductDescription("美味纯天然");
        product.setProductIcon("E://xxx.jpg");
        product.setProductStock(100);
        product.setProductStatus(ProductStatus.UP);
        product.setCategoryType(1);

        repository.save(product);
    }
    @Test
    public void findByProductStatus() {
        List<Product> byProductStatus = repository.findByProductStatus(ProductStatus.UP);
        System.out.println(byProductStatus);
    }
}
