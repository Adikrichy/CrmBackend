package org.aldousdev.common.client;

import org.aldousdev.common.model.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", path = "/api/products")
public interface ProductServiceClient {
    @GetMapping("/{id}")
    ProductDTO getProduct(@PathVariable("id") Long id);

    @PatchMapping("/{id}/stock")
    void updateStock(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
}
