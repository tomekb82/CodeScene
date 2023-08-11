package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.domain.facility.Facility;
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