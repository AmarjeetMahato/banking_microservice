package com.auth.domain.Device.services;

import com.auth.domain.Device.dtos.GeoLocation;

public interface GeoLocationService {

    GeoLocation getLocation(String ip);
}
