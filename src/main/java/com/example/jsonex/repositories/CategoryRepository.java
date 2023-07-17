package com.example.jsonex.repositories;

import com.example.jsonex.models.dtos.CategoriesStatsDto;
import com.example.jsonex.models.entities.Category;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT " +
            "c.name AS name," +
            "COUNT(DISTINCT pc.product_id) AS productCount, " +
            "AVG(p.price) AS averagePrice, " +
            "SUM(p.price) AS totalRevenue " +
            "FROM " +
            "categories c " +
            "LEFT JOIN " +
            "products_categories pc ON c.id = pc.categories_id " +
            "LEFT JOIN " +
            "products p ON pc.product_id = p.id " +
            "GROUP BY " +
            "c.name " +
            "ORDER BY " +
            "productCount DESC", nativeQuery = true)

    List<Tuple> findAllCategoriesAndTheirProductsAvgPriceAndTotalRevenue();
}
