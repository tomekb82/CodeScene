package com.mlbn.appoint.facility.domain;

import com.mlbn.appoint.common.vo.GeoPoint;

public record FacilityAddress(String streetName, String streetNumber, String apartmentNumber,
                              String city, String zipCode, String country, GeoPoint pin) {
}
