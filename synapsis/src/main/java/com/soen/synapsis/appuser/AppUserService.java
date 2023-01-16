package com.soen.synapsis.appuser;

import org.springframework.stereotype.Component;

@Component
public class AppUserService {

    public AppUser getAppUser() {
        return new AppUser(1L, "joe", "1234", "joe@mail.com", Role.USER);
    }
}
