package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCompanyProfileService {

    private CompanyProfileRepository companyProfileRepository;

    @Autowired
    public UpdateCompanyProfileService(CompanyProfileRepository companyProfileRepository) {
        this.companyProfileRepository = companyProfileRepository;
    }

    public String updateProfile(UpdateCompanyProfileRequest request) {
        if (!AppUser.isUserAuthenticated() || AppUser.getAuthenticatedUser().getRole() != Role.COMPANY) {
            return "redirect:/";
        }

        AppUser appUser = AppUser.getAuthenticatedUser();
        CompanyProfile profile = companyProfileRepository.findByAppUser(appUser);

        if (profile == null) {
            throw new IllegalStateException("The user does not have a profile.");
        }

        profile.setWebsite(request.getWebsite());
        profile.setCompanySize(request.getCompanySize());
        profile.setLocation(request.getLocation());
        profile.setIndustry(request.getIndustry());
        profile.setSpeciality(request.getSpeciality());

        companyProfileRepository.save(profile);

        return "redirect:/user/" + appUser.getId();
    }

}
