package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.vo.Money;

import java.util.Set;

public record Product(ProductId id,
                      ProductStatus status,
                      String name,
                      String description,
                      ProductCategory category,
                      Money price,
                      Set<Employee> employees,
                      Set<ConfigurationSlot> configurationSlots,
                      Set<ClosedSlot> closedSlots) {

    public boolean isActive(){
        return status().equals(ProductStatus.ACTIVE);
    }
}
