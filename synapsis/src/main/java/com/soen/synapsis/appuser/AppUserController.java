package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Entry point for user requests to interface with AppUser-related functionality.
 */
@Controller
public class AppUserController {

    private final AppUserService appUserService;
    private final ConnectionService connectionService;
    private final AuthService authService;

    @Autowired
    public AppUserController(AppUserService appUserService, ConnectionService connectionService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = new AuthService();
    }

    public AppUserController(AppUserService appUserService, ConnectionService connectionService, AuthService authService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = authService;
    }

    /**
     * Allows user to access their profile page without specifying their id.
     * @return The authenticated user's profile page. If they are not authenticated, redirects to home page.
     */
    @GetMapping("/user")
    public String redirectToAuthenticatedUserProfile() {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        String userProfileURL = "redirect:/user/" + authService.getAuthenticatedUser().getId();

        return userProfileURL;
    }

    /**
     * Retrieves a user's profile page, both for standard and company users.
     * @param uid the user's page to retrieve.
     * @param model Allows for data to be passed to view.
     * @return View to profile page if the requester is authenticated, else redirects to home page.
     */
    @GetMapping("/user/{uid}")
    public String getAppUser(@PathVariable Long uid, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();
        if (appUser.getRole() == Role.ADMIN) {
            return "redirect:/";
        }

        model.addAttribute("role", appUser.getRole());
        boolean isConnectedWith = connectionService.isConnectedWith(authService.getAuthenticatedUser().getId(), uid);
        boolean isPendingConnectionWhenSentConnection = connectionService.isPendingConnectionWith(authService.getAuthenticatedUser().getId(), uid); // You sent a connection request and are waiting on the person
        boolean isPendingConnectionWhenReceivingConnection = connectionService.isPendingConnectionWith(uid, authService.getAuthenticatedUser().getId()); // Another person sent a connection request to you, and is waiting for you

        model.addAttribute("id", appUser.getId());
        model.addAttribute("name", appUser.getName());
        model.addAttribute("email", appUser.getEmail());
        model.addAttribute("profilePicture", appUser.getProfilePicture() != null ? appUser.getProfilePicture().getImage() : "");
        model.addAttribute("isConnectedWith", isConnectedWith);
        model.addAttribute("isPendingConnectionWhenSentConnection", isPendingConnectionWhenSentConnection);
        model.addAttribute("isPendingConnectionWhenReceivingConnection", isPendingConnectionWhenReceivingConnection);
        model.addAttribute("role", appUser.getRole());
        model.addAttribute("myRole", authService.getAuthenticatedUser().getRole());
        model.addAttribute("myId", authService.getAuthenticatedUser().getId());
        if(appUser.getRole() ==  Role.RECRUITER) {
            model.addAttribute("companyId", appUser.getCompany().getId());
        }

        model.addAttribute("showControls", authService.getAuthenticatedUser().getId() == uid);

        if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = appUser.getCompanyProfile();
            if (companyProfile == null)
                companyProfile = new CompanyProfile();

            model.addAttribute("description", companyProfile.getDescription());
            model.addAttribute("website", companyProfile.getWebsite());
            model.addAttribute("industry", companyProfile.getIndustry());
            model.addAttribute("companySize", companyProfile.getCompanySize());
            model.addAttribute("location", companyProfile.getLocation());
            model.addAttribute("speciality", companyProfile.getSpeciality());
            return "pages/companypage";
        }

        AppUserProfile profile = appUser.getAppUserProfile();
        if (profile == null)
            profile = new AppUserProfile();

        model.addAttribute("description", profile.getDescription());
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

    /**
     * Allows user to view search results for other users.
     * @param name Name to search for
     * @param model Allows data to be passed to view
     * @return Returns search results if the user is authenticated, else redirects to home page.
     */
    @GetMapping("/search")
    public String getUsersLikeName(@RequestParam String name, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<AppUser> users = appUserService.getRegularUsersLikeName(name, authService.getAuthenticatedUser().getId());
        model.addAttribute("users", users);
        return "pages/usersearchpage";
    }

    /**
     * Sample page for verifying if user is authenticated.
     * @return View containing private user page.
     */
    @GetMapping("/privateuser")
    @ResponseBody
    public String getUserData() {
        return "This is the user page";
    }

    /**
     * Sample page for verifying if user is authenticated as admin.
     * @return View containing admin page
     */
    @GetMapping("/admin")
    @ResponseBody
    public String getAdminData() {
        return "This is the admin page";
    }

    /**
     * Allows company to mark a CANDIDATE user to a RECRUITER for their company.
     * @param id The id of the user to mark as a recruiter
     * @return View containing user profile. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/company/markCandidateToRecruiter")
    public String markCandidateToRecruiter(@RequestParam("appUserId") Long id) {
        try {
            if(!authService.doesUserHaveRole(Role.COMPANY)) {
                throw new IllegalStateException("You must be a company to mark candidates as recruiters.");
            }
            Optional<AppUser> optionalAppUser = appUserService.getAppUser(id);

            if(optionalAppUser.isEmpty()) {
                return "redirect:/";
            }

            AppUser appUser = optionalAppUser.get();

            if(appUser.getRole() != Role.CANDIDATE) {
                throw new IllegalStateException("The user must be a candidate to be marked as a recruiter.");
            }

            appUserService.markCandidateToRecruiter(appUser, authService.getAuthenticatedUser());
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        }
        catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Allows companies to unmark a recruiter belonging to them back to CANDIDATE role.
     * @param id The id of the recruiter to unmark
     * @return View containing profile page of unmarked user.
     * If the requester is not authenticated, redirect to home page.
     */
    @PostMapping("/company/unmarkRecruiterToCandidate")
    public String unmarkRecruiterToCandidate(@RequestParam("appUserId") Long id) {
        try {
            if (!authService.doesUserHaveRole(Role.COMPANY)) {
                throw new IllegalStateException("You must be a company to unmark recruiters as candidates.");
            }
            Optional<AppUser> optionalAppUser = appUserService.getAppUser(id);

            if (optionalAppUser.isEmpty()) {
                return "redirect:/";
            }

            AppUser appUser = optionalAppUser.get();

            if (appUser.getRole() != Role.RECRUITER) {
                throw new IllegalStateException("The user must be a recruiter to be unmarked as a candidate.");
            }

            if(appUser.getCompany().getId() != authService.getAuthenticatedUser().getId()) {
                throw new IllegalStateException("The recruiter is not part of your company.");
            }

            appUserService.unmarkRecruiterToCandidate(appUser, authService.getAuthenticatedUser());
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        }
        catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Allows user to access the update user page.
     * @return View containing the update user page.
     */
    @GetMapping("/updateuserpage")
    public String getUpdateUserProfile() {
        return "pages/updateuserpage";
    }

}
