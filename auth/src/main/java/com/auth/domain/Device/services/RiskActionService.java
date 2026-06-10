package com.auth.domain.Device.services;

import com.auth.domain.Users.entity.User;

public interface RiskActionService {

    String handleSuspiciousLogin(User user);

}
