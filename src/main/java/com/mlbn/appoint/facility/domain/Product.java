package com.mlbn.appoint.facility.domain;

import com.mlbn.appoint.common.vo.Money;

import java.util.Set;

public record Product(ProductId id,
                      ProductStatus status,
                      String name,
                      String description,
                      ProductCategory category,
                      Money price,
                      Set<Employee> employees,
                      Set<ConfigurationSlot> configurationSlots) {

    public boolean isActive(){
        return status().equals(ProductStatus.ACTIVE);
    }
}
