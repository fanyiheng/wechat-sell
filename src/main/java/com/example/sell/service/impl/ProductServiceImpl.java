package com.example.sell.service.impl;

import com.example.sell.dto.CarDto;
import com.example.sell.entity.Product;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.enums.ProductStatus;
import com.example.sell.exception.SellException;
import com.example.sell.repository.ProductRepository;
import com.example.sell.service.ProductService;
import com.example.sell.service.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private RedisLock redisLock;

    @Override
    public Product findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<Product> findUpAll() {
        return repository.findByProductStatus(ProductStatus.UP);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public void increaseStock(List<CarDto> carDtos) {
        for (CarDto carDto : carDtos) {
            Product product = repository.findOne(carDto.getProductId());
            if (product == null) {
                log.error("【增加库存】商品不存在，商品ID={}", carDto.getProductId());
                throw new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
            }
            Integer result = product.getProductStock() + carDto.getProductQuantity();
            product.setProductStock(result);
            repository.save(product);
        }
    }

    public static final int TIMEOUT = 10 * 1000;
    @Override
    @Transactional
    public void decreaseStock(List<CarDto> carDtos) {
        for (CarDto carDto : carDtos) {
            String expiredTime = String.valueOf(System.currentTimeMillis() + TIMEOUT);
            //获取分布式锁
            if (!redisLock.lock(carDto.getProductId(),expiredTime )) {
                log.error("【redis分布式锁】获取不到锁");
                throw new SellException(ExceptionCode.LOCK_FAILED,
                        "哎哟喂，人数太多，抢不到宝贝，换个姿势试试~~");
            }
            Product product = repository.findOne(carDto.getProductId());
            if (product == null) {
                log.error("【增加库存】商品不存在，商品ID={}", carDto.getProductId());
                throw new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
            }
            Integer result = product.getProductStock() - carDto.getProductQuantity();
            if (result < 0) {
                log.error("【增加库存】商品库存不足，商品ID={},商品现有库存={}，待减少的库存={}", carDto.getProductId(),
                        product.getProductStock(), carDto.getProductQuantity());
                throw new SellException(ExceptionCode.PRODUCT_INSUFFICIENT);
            }
            //释放锁
            redisLock.unlock(carDto.getProductId(),expiredTime );
            product.setProductStock(result);
        }
    }

    @Override
    public Product onSale(String productId) {
        Product productInfo = repository.findOne(productId);
        if (productInfo == null) {
            throw new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatus.UP) {
            throw new SellException(ExceptionCode.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatus.UP);
        return repository.save(productInfo);
    }

    @Override
    public Product offSale(String productId) {
        Product productInfo = repository.findOne(productId);
        if (productInfo == null) {
            throw new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatus.DOWN) {
            throw new SellException(ExceptionCode.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(ProductStatus.DOWN);
        return repository.save(productInfo);
    }
}
