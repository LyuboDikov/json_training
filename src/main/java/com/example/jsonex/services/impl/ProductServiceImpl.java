package com.example.jsonex.services.impl;

import com.example.jsonex.models.dtos.ProductSeedDto;
import com.example.jsonex.models.entities.Product;
import com.example.jsonex.models.entities.User;
import com.example.jsonex.repositories.ProductRepository;
import com.example.jsonex.services.CategoryService;
import com.example.jsonex.services.ProductService;
import com.example.jsonex.services.UserService;
import com.example.jsonex.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.example.jsonex.constants.GlobalConstant.RESOURCES_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTS_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, Gson gson,
                              ModelMapper modelMapper, ValidationUtil validationUtil,
                              UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {

        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(
                Path.of(RESOURCES_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDto[] productSeedDtos =
                gson.fromJson(fileContent, ProductSeedDto[].class);


        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                product.setSeller(userService.findRandomUser());

                if (product.getPrice().compareTo(BigDecimal.valueOf(500L)) > 0) {

                    User buyer = userService.findRandomUser();

                    while(buyer.equals(product.getSeller())) {
                        buyer = userService.findRandomUser();
                    }

                    product.setBuyer(buyer);
                }

                product.setCategories(categoryService.findRandomCategories());

                return product;
                })
                .forEach(productRepository::save);

    }
}
