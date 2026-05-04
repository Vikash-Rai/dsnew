package com.equabli.collectprism.approach.repository;

import com.equabli.collectprism.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for conf.product.
 * Used exclusively by EnrichmentCacheServiceImpl to load product.debtcategory_id
 * once per productId, then cache it in Caffeine as a scalar Integer.
 *
 * FIX: Added findDebtCategoryIdById() projection query.
 * The old approach loaded the full Product entity just to call .getDebtCategoryId().
 * This projection fetches only the one column that enrichment actually needs,
 * keeping the Caffeine heap footprint minimal (Integer vs full entity object graph).
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Returns only product.debtcategory_id for the given productId.
     * Used by EnrichmentCacheServiceImpl.getProductDebtCategoryId().
     */
    @Query("SELECT p.debtCategoryId FROM Product p WHERE p.productId = :productId")
    Optional<Integer> findDebtCategoryIdById(@Param("productId") Integer productId);
}