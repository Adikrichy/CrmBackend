package org.aldousdev.analyticsservice.mapper;

import org.aldousdev.analyticsservice.dto.ProductAnalyticsDTO;
import org.aldousdev.analyticsservice.entity.ProductAnalytics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductAnalyticsMapper {
    
    @Mapping(target = "id", ignore = true)
    ProductAnalytics toEntity(ProductAnalyticsDTO dto);
    
    ProductAnalyticsDTO toDTO(ProductAnalytics entity);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProductAnalyticsDTO dto, @MappingTarget ProductAnalytics entity);
} 