package com.example.sell.repository;

import com.example.sell.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void saveTest(){
        ProductCategory category = new ProductCategory();
        category.setCategoryName("测试2");
        category.setCategoryType(2);
        category = repository.save(category);
        Assert.assertNotNull(category);
    }

    @Test
    public void findTest(){
        List<ProductCategory> list = repository.findAll();
        System.out.println(list.get(0).getCategoryName());
    }

    @Test
    public void findByCategoryTypeTest(){
        List<ProductCategory> byCategoryTypeIn = repository.findByCategoryTypeIn(Arrays.asList(1, 2));// ctrl alt v

    }
}
