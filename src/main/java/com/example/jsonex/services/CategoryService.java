package com.example.jsonex.services;

import com.example.jsonex.models.dtos.CategoriesStatsDto;
import com.example.jsonex.models.entities.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;
    Set<Category> findRandomCategories();
    List<CategoriesStatsDto> findAllCategoriesByProductsCount();
}
