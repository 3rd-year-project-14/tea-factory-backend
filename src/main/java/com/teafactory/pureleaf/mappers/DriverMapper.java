package com.teafactory.pureleaf.mappers;

import com.teafactory.pureleaf.driverProcess.dto.DriverDTO;
import com.teafactory.pureleaf.driverProcess.entity.Driver;
import com.teafactory.pureleaf.routes.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    // Entity → DTO
    @Mapping(source = "factory.factoryId", target = "factoryId")
    @Mapping(source = "factory.name", target = "factoryName")
    @Mapping(source = "factory.mapUrl", target = "factoryMapUrl")
    @Mapping(source = "user.id", target = "userId")
    DriverDTO toDto(Driver driver);


    // DTO → Entity
    @Mapping(source = "factoryId", target = "factory.factoryId")
    @Mapping(source = "userId", target = "user.id")
    Driver toEntity(DriverDTO dto);
}

