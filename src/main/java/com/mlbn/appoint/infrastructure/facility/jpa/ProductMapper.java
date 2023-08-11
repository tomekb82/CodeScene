package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.domain.facility.Product;
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