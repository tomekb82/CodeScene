package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.common.vo.GeoPoint;
import com.mlbn.appoint.facility.domain.FacilityAddress;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
class FacilityAddressEntity{
    String streetName;
    String streetNumber;
    String apartmentNumber;
    String city;
    String zipCode;
    String country;
    double geoLat;
    double geoLng;

    public FacilityAddressEntity(String streetName, String streetNumber, String apartmentNumber, String city,
                                 String zipCode, String country, double geoLat, double geoLng) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
    }

    public static FacilityAddressEntity from(FacilityAddress address) {
        return new FacilityAddressEntity(address.streetName(), address.streetNumber(), address.apartmentNumber(), address.city(),
                address.zipCode(), address.country(), address.pin().lat(), address.pin().lng());
    }

    public FacilityAddress from() {
        return new FacilityAddress(streetName, streetNumber, apartmentNumber, city,
                zipCode, country, new GeoPoint(geoLat, geoLng));
    }
}
