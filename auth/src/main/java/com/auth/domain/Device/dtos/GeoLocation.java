package com.auth.domain.Device.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoLocation {

    private String ip;
    private String country;
    private String region;
    private String city;
    private String timezone;
    private String language;
}