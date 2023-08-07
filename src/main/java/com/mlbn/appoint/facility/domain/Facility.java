package com.mlbn.appoint.facility.domain;

import com.mlbn.appoint.common.vo.PhoneNumber;

import java.util.Set;

public record Facility(FacilityId id, String name, FacilityAddress address, PhoneNumber phoneNumber, Set<Product> products){}
