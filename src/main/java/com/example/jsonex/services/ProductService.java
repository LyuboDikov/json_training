package com.example.jsonex.services;

import com.example.jsonex.models.dtos.ProductNamePriceAndSellerDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNamePriceAndSellerDto> findAllProductsInRangeOrderedByPrice(BigDecimal lowerBound, BigDecimal upperBound);
}
