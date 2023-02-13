package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Controller
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/{uid}")
    public String getAppUser(@PathVariable Long uid, Model model) {
        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();
        AppUserProfile profile = appUser.getAppUserProfile();

        model.addAttribute("id", appUser.getId());
        model.addAttribute("name", appUser.getName());
        model.addAttribute("email", appUser.getEmail());

        if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = appUser.getCompanyProfile();
            model.addAttribute("website", companyProfile.getWebsite());
            model.addAttribute("industry", companyProfile.getIndustry());
            model.addAttribute("companySize", companyProfile.getCompanySize());
            model.addAttribute("location", companyProfile.getLocation());
            model.addAttribute("speciality", companyProfile.getSpeciality());
            return "pages/companypage";
        }

            model.addAttribute("education", profile.getEducation());
            model.addAttribute("skill", profile.getSkill());
            model.addAttribute("work", profile.getWork());
            model.addAttribute("course", profile.getCourse());
            model.addAttribute("phone", profile.getPhone());
            model.addAttribute("volunteering", profile.getVolunteering());
            model.addAttribute("project", profile.getProject());
            model.addAttribute("award", profile.getAward());
            model.addAttribute("language", profile.getLanguage());

        return "pages/userpage";




    }







    @GetMapping("/updateuserpage/{uid}")
    public String getUpdateAppUser(@PathVariable Long uid, Model model) {
        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();

        AppUserProfile profile = appUser.getAppUserProfile();

        model.addAttribute("name", appUser.getName());
        model.addAttribute("education", profile.getEducation());
        model.addAttribute("skill", profile.getSkill());
        model.addAttribute("work", profile.getWork());
        model.addAttribute("course", profile.getCourse());
        model.addAttribute("phone", profile.getPhone());
        model.addAttribute("volunteering", profile.getVolunteering());
        model.addAttribute("project", profile.getProject());
        model.addAttribute("award", profile.getAward());
        model.addAttribute("language", profile.getLanguage());

        return "pages/updateuserpage";

    }




















// create a method called  update, get(sends u to the page where u will update), post(update database and send u to the correct page),
    @GetMapping("/privateuser")
    @ResponseBody
    public String getUserData() {
        return "This is the user page";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String getAdminData() {
        return "This is the admin page";
    }




    @GetMapping("/updateuserpage")
    public String getUpdateUserProfile() {
            return "pages/updateuserpage";
    }


//    @PostMapping("/userpage")
//    public String update(AppUserProfile profile) {
//        AppUserService.UpdateProfile(profile);
//        return "pages/userpage";
//    }





}
