package com.mlbn.appoint.infrastructure.facility.jpa

import com.mlbn.appoint.domain.facility.Product
import org.mapstruct.factory.Mappers
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class ProductMapperTest extends Specification{

    def 'should map all fields'() {
        given:
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class)
        Product product = aProduct()

        when:
        ProductEntity entity = mapper.map(product)
        Product mapped = mapper.map(entity)

        then:
        mapped.status() == product.status()
        mapped.configurationSlots() == product.configurationSlots()
        mapped.closedSlots() == product.closedSlots()
    }
}
