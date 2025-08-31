package com.teafactory.pureleaf.routes.dto;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetailsDTO {
    private Long routeId;

    private String name;

    private String routeCode;
}

