package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service class to update the profile of a company user.
 */
@Service
public class UpdateCompanyProfileService {

    private CompanyProfileRepository companyProfileRepository;

    @Autowired
    public UpdateCompanyProfileService(CompanyProfileRepository companyProfileRepository) {
        this.companyProfileRepository = companyProfileRepository;
    }

    /**
     * Retrieve the profile of a company user.
     * @param appUser the object representing the company user.
     * @return the CompanyProfile of a company user.
     */
    public CompanyProfile findAppUserProfile(AppUser appUser) {
        return companyProfileRepository.findByAppUser(appUser);
    }

    /**
     * Update the profile of the company user based on his request.
     * @param request an object carrying the request of a company user to update their profile.
     * @param appUser the object representing the company user.
     * @return the company user profile page.
     */
    public String updateProfile(UpdateCompanyProfileRequest request, AppUser appUser) {
        CompanyProfile profile = findAppUserProfile(appUser);

        if (profile == null) {
            throw new IllegalStateException("The user does not have a profile.");
        }

        profile.setDescription(request.getDescription());
        profile.setWebsite(request.getWebsite());
        profile.setCompanySize(request.getCompanySize());
        profile.setLocation(request.getLocation());
        profile.setIndustry(request.getIndustry());
        profile.setSpeciality(request.getSpeciality());

        companyProfileRepository.save(profile);

        return "redirect:/user/" + appUser.getId();
    }

}
