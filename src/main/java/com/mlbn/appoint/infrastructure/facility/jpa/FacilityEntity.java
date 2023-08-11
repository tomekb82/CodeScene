package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.shared.vo.PhoneNumber;
import com.mlbn.appoint.domain.facility.Facility;
import com.mlbn.appoint.domain.facility.FacilityId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "facilites")
@NoArgsConstructor
class FacilityEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Embedded
    private FacilityAddressEntity address;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<ProductEntity> products;

    private String phoneNumber;

    public FacilityEntity(String name, FacilityAddressEntity address,
                          String phoneNumber,
                          Set<ProductEntity> products) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.products = products;
    }

    public static FacilityEntity from(Facility facility) {
        return new FacilityEntity(facility.name(), FacilityAddressEntity.from(facility.address()),
                facility.phoneNumber().number(),
                ProductEntity.from(facility.products())
        );
    }

    public Facility from() {
        return new Facility(new FacilityId(id),
                name,
                address.from(),
                new PhoneNumber(phoneNumber),
                products.stream().map(ProductEntity::from).collect(Collectors.toSet())
        );
    }
}
