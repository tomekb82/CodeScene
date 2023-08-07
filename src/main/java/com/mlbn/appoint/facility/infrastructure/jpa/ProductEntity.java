package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.common.vo.Money;
import com.mlbn.appoint.facility.domain.Product;
import com.mlbn.appoint.facility.domain.ProductCategory;
import com.mlbn.appoint.facility.domain.ProductId;
import com.mlbn.appoint.facility.domain.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor
class ProductEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatusEntity status;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<ConfigurationSlotEntity> slots;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<EmployeeEntity> employees;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal price;

    public ProductEntity(ProductStatusEntity status, String name, String description,
                         String category, BigDecimal price, Set<EmployeeEntity> employees, Set<ConfigurationSlotEntity> slots) {
        this.status = status;
        this.name = name;
        this.description = description;
        this.category = category;
        this.employees = employees;
        this.slots = slots;
        this.price = price;
    }

    public static ProductEntity from(Product product) {
        return new ProductEntity(ProductStatusEntity.valueOf(product.status().name()),
                product.name(), product.description(), product.category().name(), product.price().getAmount(),
                EmployeeEntity.from(product.employees()),
                ConfigurationSlotEntity.from(product.configurationSlots()));
    }

    public static Set<ProductEntity> from(Set<Product> products) {
        return products.stream()
                .map(ProductEntity::from)
                .collect(Collectors.toSet());
    }

    public Product from() {
        return new Product(new ProductId(id), ProductStatus.valueOf(status.name()), name, description,
                ProductCategory.valueOf(category),
                Money.of(price).get(), //TODO
                employees.stream().map(EmployeeEntity::toEmployee).collect(Collectors.toSet()),
                slots.stream().map(ConfigurationSlotEntity::toConfigurationSlot).collect(Collectors.toSet()));
    }
}
