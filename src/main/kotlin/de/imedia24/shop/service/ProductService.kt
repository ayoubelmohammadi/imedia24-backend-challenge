package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductRequest.Companion.toEntity
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.exception.ProductAlreadyExistsException
import de.imedia24.shop.exception.ProductNotExistsException
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse? {
        return productRepository.findBySku(sku)?.toProductResponse()
    }

    fun findProductsBySkuList(skus: List<String>): List<ProductResponse> {
        return productRepository.findAllBySkuIn(skus).map { it.toProductResponse() }
    }

    fun addProduct(productRequest: ProductRequest): ProductResponse {
        if (productRepository.existsById(productRequest.sku)) {
            throw ProductAlreadyExistsException("Product with SKU ${productRequest.sku} already exists")
        }
        val product = productRequest.toEntity()
        productRepository.save(product)
        return product.toProductResponse()
    }

    fun updateProduct(sku: String, productUpdateRequest: ProductUpdateRequest): ProductResponse {
        val product = productRepository.findById(sku).orElseThrow {
            ProductNotExistsException("Product with SKU $sku not found")
        }

        if (productUpdateRequest.name != null) product.name = productUpdateRequest.name
        if (productUpdateRequest.description != null) product.description = productUpdateRequest.description
        if (productUpdateRequest.price != null) product.price = productUpdateRequest.price

        productRepository.save(product)

        return product.toProductResponse()
    }
}
