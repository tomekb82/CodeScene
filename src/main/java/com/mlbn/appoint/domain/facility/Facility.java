package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.vo.PhoneNumber;

import java.util.Set;

public record Facility(FacilityId id, String name, FacilityAddress address, PhoneNumber phoneNumber, Set<Product> products){}
