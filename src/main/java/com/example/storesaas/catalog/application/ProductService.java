package com.example.storesaas.catalog.application;

import com.example.storesaas.catalog.api.ProductSnapshot;
import com.example.storesaas.catalog.dto.CategoryDTO;
import com.example.storesaas.catalog.dto.ProductDTO;
import com.example.storesaas.catalog.entity.Product;
import com.example.storesaas.catalog.vo.CategoryVO;
import com.example.storesaas.catalog.vo.ProductVO;
import com.example.storesaas.catalog.vo.PublicCategoryVO;
import com.example.storesaas.catalog.vo.PublicProductVO;

import java.util.List;

public interface ProductService {

    List<CategoryVO> categories();

    List<PublicCategoryVO> publicCategories(Long tenantId);

    CategoryVO createCategory(CategoryDTO request);

    List<ProductVO> products();

    List<PublicProductVO> publicProducts(Long tenantId, Long categoryId);

    ProductVO createProduct(ProductDTO request);

    ProductVO updateProduct(Long id, ProductDTO request);

    ProductVO setProductStatus(Long id, Integer status);

    CategoryVO setCategoryStatus(Long id, Integer status);

    void deleteProduct(Long id);

    Product tenantProduct(Long tenantId, Long productId);

    ProductSnapshot getTenantProduct(Long tenantId, Long productId);
}
