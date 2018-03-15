package com.example.sell.controller;

import com.example.sell.entity.Product;
import com.example.sell.entity.ProductCategory;
import com.example.sell.service.CategoryService;
import com.example.sell.service.ProductService;
import com.example.sell.util.ResultFactory;
import com.example.sell.vo.CategoryWithProductVO;
import com.example.sell.vo.ProductVO;
import com.example.sell.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/list")
    public Result list(){
        //1.查询所有上架商品
        List<Product> allUpProducts = productService.findUpAll();
        List<Integer> categoryTypeIds = allUpProducts.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        //2.查询类目
        List<ProductCategory> categoryTypes = categoryService.findByCategoryTypeIn(categoryTypeIds);
        //3.拼装数据
        List<CategoryWithProductVO> categoryWithProductVOs = new ArrayList<>();
        for (ProductCategory productCategory : categoryTypes){
            CategoryWithProductVO categoryWithProductVO = new CategoryWithProductVO();
            categoryWithProductVO.setCategoryType(productCategory.getCategoryType());
            categoryWithProductVO.setCategoryName(productCategory.getCategoryName());
            List<ProductVO> productVOs = new ArrayList<>();
            for(Product product : allUpProducts){
                if(product.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductVO productVO = new ProductVO();
                    BeanUtils.copyProperties(product, productVO);
                    productVOs.add(productVO);
                }
            }
            categoryWithProductVO.setProductVOs(productVOs);
            categoryWithProductVOs.add(categoryWithProductVO);
        }
        return ResultFactory.success(categoryWithProductVOs);
    }
}
