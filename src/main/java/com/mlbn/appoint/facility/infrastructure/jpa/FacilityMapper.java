package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.facility.domain.Facility;
import org.mapstruct.Mapper;

@Mapper
interface FacilityMapper {

    default FacilityEntity map(Facility facility){
        return FacilityEntity.from(facility);
    }

    default Facility map(FacilityEntity entity){
        return entity.from();
    }

}