package com.example.jsonex.services.impl;

import com.example.jsonex.repositories.CategoryRepository;
import com.example.jsonex.services.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.jsonex.constants.GlobalConstant.RESOURCES_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String CATEGORIES_FILE_NAME = "categories.json";

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));
    }
}
