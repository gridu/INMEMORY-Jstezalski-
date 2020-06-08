package com.ignite.Project1.controller;

import com.ignite.Project1.model.Product;
import com.ignite.Project1.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private ProductService service;

    @GetMapping("/{uniq_id}")
    public Product getProduct(@PathVariable String uniq_id) {
        return service.getProduct(uniq_id);
    }

    @PutMapping("/{uniq_id}")
    public Product updateListPrice(@PathVariable String uniq_id,
                                   @RequestParam String listPrice) {
        return service.updateListPrice(uniq_id, listPrice);
    }

}