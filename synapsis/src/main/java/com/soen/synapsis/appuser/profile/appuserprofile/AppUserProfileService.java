package com.soen.synapsis.appuser.profile.appuserprofile;

import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import org.springframework.stereotype.Component;

@Component
public class AppUserProfileService extends AppUserService {
    private AppUserProfileRepository appUserProfileRepository;

    public AppUserProfileService(AppUserRepository appUserRepository) {
        super(appUserRepository);
    }

}
