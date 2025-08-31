package com.teafactory.pureleaf.routes.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRouteDTO {
    private String name;

    private String startLocation;

    private String endLocation;

    private Long factoryId;

    private String routeCode;
}

