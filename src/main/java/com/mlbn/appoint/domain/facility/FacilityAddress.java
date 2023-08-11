package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.vo.GeoPoint;

public record FacilityAddress(String streetName, String streetNumber, String apartmentNumber,
                              String city, String zipCode, String country, GeoPoint pin) {
}
