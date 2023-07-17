package com.example.jsonex.services.impl;

import com.example.jsonex.models.dtos.CategoriesStatsDto;
import com.example.jsonex.models.dtos.CategorySeedDto;
import com.example.jsonex.models.entities.Category;
import com.example.jsonex.repositories.CategoryRepository;
import com.example.jsonex.services.CategoryService;
import com.example.jsonex.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.jsonex.constants.GlobalConstant.RESOURCES_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE_NAME = "categories.json";
    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Gson gson,
                               ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos =
                gson.fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomCategories() {

        Set<Category> categorySet = new HashSet<>();
        int categoriesCount = ThreadLocalRandom.current().nextInt(1, 3);

        long totalCategoriesCount = categoryRepository.count();

        for (int i = 0; i < categoriesCount; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, totalCategoriesCount + 1);

            categorySet.add(categoryRepository.findById(randomId).orElse(null));
        }

        return categorySet;
    }

    @Override
    public List<CategoriesStatsDto> findAllCategoriesByProductsCount() {

        List<Tuple> categoriesTuple = categoryRepository.findAllCategoriesAndTheirProductsAvgPriceAndTotalRevenue();

        List<CategoriesStatsDto> categoriesStatsDtos = categoriesTuple
                .stream()
                .map(t -> new CategoriesStatsDto(
                        t.get(0, String.class),
                        t.get(1, Long.class),
                        t.get(2, BigDecimal.class),
                        t.get(3, BigDecimal.class)
                )).collect(Collectors.toList());

        return categoriesStatsDtos;
    }
}
