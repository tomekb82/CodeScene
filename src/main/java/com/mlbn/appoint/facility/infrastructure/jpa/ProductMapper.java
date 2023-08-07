package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.facility.domain.Product;
import org.mapstruct.Mapper;

@Mapper
interface ProductMapper {

    default ProductEntity map(Product product){
        return ProductEntity.from(product);
    }

    default Product map(ProductEntity entity){
        return entity.from();
    }

}