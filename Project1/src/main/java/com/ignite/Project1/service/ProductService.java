package com.ignite.Project1.service;

import com.ignite.Project1.exception.ProductNotFoundException;
import com.ignite.Project1.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMetrics;
import org.apache.ignite.resources.CacheNameResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProductService {

    @CacheNameResource
    private final IgniteCache<String, Product> productCache;

    public Product getProduct(String uniq_id) {
        Product product = productCache.get(uniq_id);

        if (product == null) {
            throw new ProductNotFoundException("There is no product with id: " + uniq_id);
        }

        CacheMetrics cm = productCache.metrics();
        log.info("Cache size={}, Cache hits={}, Cache misses={}", cm.getCacheSize(), cm.getCacheHits(), cm.getCacheMisses());

        return product;
    }

    public Product updateListPrice(String uniq_id, String listPrice) {
        Product productToUpdate = productCache.get(uniq_id);

        if (productToUpdate == null) {
            throw new ProductNotFoundException("There is no product with id: " + uniq_id);
        }

        productToUpdate.setList_price(listPrice);
        productCache.put(uniq_id, productToUpdate);
        return productToUpdate;
    }
}


