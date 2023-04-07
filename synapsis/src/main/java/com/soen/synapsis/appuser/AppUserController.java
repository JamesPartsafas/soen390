package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.websockets.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final ChatService chatService;

    @Autowired
    public AppUserController(AppUserService appUserService, ConnectionService connectionService, ChatService chatService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = new AuthService();
        this.chatService = chatService;
    }

    public AppUserController(AppUserService appUserService, ConnectionService connectionService, AuthService authService, ChatService chatService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = authService;
        this.chatService = chatService;
    }

    /**
     * Allows user to access their profile page without specifying their id.
     *
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
     *
     * @param uid   the user's page to retrieve.
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

        AppUser authedUser = authService.getAuthenticatedUser();

        boolean isConnectedWith = connectionService.isConnectedWith(authedUser.getId(), uid);
        boolean isPendingConnectionWhenSentConnection = connectionService.isPendingConnectionWith(authedUser.getId(), uid); // You sent a connection request and are waiting on the person
        boolean isPendingConnectionWhenReceivingConnection = connectionService.isPendingConnectionWith(uid, authedUser.getId()); // Another person sent a connection request to you, and is waiting for you
        int numberOfConnections = connectionService.getNumberOfConnections(uid);


        model.addAttribute("currentAppUser", appUser);
        ProfilePicture profilePicture = appUser.getProfilePicture();

        if (profilePicture == null) {
            model.addAttribute("profilePicture", "");
            model.addAttribute("coverPicture", "");
        } else {
            model.addAttribute("profilePicture", profilePicture.getImage());
            model.addAttribute("coverPicture", profilePicture.getCoverImage());
        }

        model.addAttribute("isConnectedWith", isConnectedWith);
        model.addAttribute("isPendingConnectionWhenSentConnection", isPendingConnectionWhenSentConnection);
        model.addAttribute("isPendingConnectionWhenReceivingConnection", isPendingConnectionWhenReceivingConnection);
        model.addAttribute("numberOfConnections", numberOfConnections);
        model.addAttribute("myRole", authedUser.getRole());
        model.addAttribute("myId", authedUser.getId());
        if (appUser.getRole() == Role.RECRUITER) {
            model.addAttribute("companyId", appUser.getCompany().getId());
        }

        model.addAttribute("isAuthedUserProfile", authedUser.getId() == uid);

        if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = appUser.getCompanyProfile();
            if (companyProfile == null)
                companyProfile = new CompanyProfile();

            model.addAttribute("companyProfile", companyProfile);

            return "pages/companypage";
        }

        AppUserProfile profile = appUser.getAppUserProfile();
        if (profile == null)
            profile = new AppUserProfile();

        model.addAttribute("profile", profile);

        return "pages/userpage";
    }

    /**
     * Allows user to view search results for other users.
     *
     * @param name  Name to search for
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
     *
     * @return View containing private user page.
     */
    @GetMapping("/privateuser")
    @ResponseBody
    public String getUserData() {
        return "This is the user page";
    }

    /**
     * Sample page for verifying if user is authenticated as admin.
     *
     * @return View containing admin page
     */
    @GetMapping("/admin")
    @ResponseBody
    public String getAdminData() {
        return "This is the admin page";
    }

    /**
     * Allows company to mark a CANDIDATE user to a RECRUITER for their company.
     *
     * @param id The id of the user to mark as a recruiter
     * @return View containing user profile. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/company/markCandidateToRecruiter")
    public String markCandidateToRecruiter(@RequestParam("appUserId") Long id) {
        try {
            if (!authService.doesUserHaveRole(Role.COMPANY)) {
                throw new IllegalStateException("You must be a company to mark candidates as recruiters.");
            }
            Optional<AppUser> optionalAppUser = appUserService.getAppUser(id);

            if (optionalAppUser.isEmpty()) {
                return "redirect:/";
            }

            AppUser appUser = optionalAppUser.get();

            if (appUser.getRole() != Role.CANDIDATE) {
                throw new IllegalStateException("The user must be a candidate to be marked as a recruiter.");
            }

            appUserService.markCandidateToRecruiter(appUser, authService.getAuthenticatedUser());
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        } catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Allows companies to unmark a recruiter belonging to them back to CANDIDATE role.
     *
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

            if (appUser.getCompany().getId() != authService.getAuthenticatedUser().getId()) {
                throw new IllegalStateException("The recruiter is not part of your company.");
            }

            appUserService.unmarkRecruiterToCandidate(appUser, authService.getAuthenticatedUser());
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        } catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Allows user to access the update user page.
     *
     * @return View containing the update user page.
     */
    @GetMapping("/updateuserpage")
    public String getUpdateUserProfile() {
        return "pages/updateuserpage";
    }

    /**
     * Allows users to save a job.
     *
     * @param jobId The id of job being saved.
     * @return View containing saved jobs. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/savejob")
    public String saveJob(@RequestParam("jobId") Long jobId) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }
        AppUser appUser = authService.getAuthenticatedUser();
        return appUserService.saveJob(jobId, appUser);
    }

    /**
     * Allows users to unsave a job.
     *
     * @param jobId The id of job being unsaved.
     * @return View containing saved jobs. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/deletesavedjob")
    public String deleteSavedJob(@RequestParam("jobId") Long jobId) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }
        AppUser appUser = authService.getAuthenticatedUser();
        return appUserService.deleteSavedJob(jobId, appUser);
    }

    /**
     * Allows administrator to mark a COMPANY user as verified.
     *
     * @param id The id of the company user to be marked as verified.
     * @return View containing user profile. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/admin/verifyCompany")
    public String markCompanyAsVerified(@RequestParam("companyUserId") Long id) {
        try {
            if (!authService.doesUserHaveRole(Role.ADMIN)) {
                throw new IllegalStateException("You must be an admin to mark a company as verified.");
            }
            Optional<AppUser> optionalAppUser = appUserService.getAppUser(id);

            if (optionalAppUser.isEmpty()) {
                return "redirect:/";
            }

            AppUser appUser = optionalAppUser.get();

            if (appUser.getRole() != Role.COMPANY) {
                throw new IllegalStateException("The user must be a company to be marked as a verified company.");
            }

            appUserService.markCompanyAsVerified(appUser);
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        } catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Allows administrator to mark a COMPANY user as non-verified.
     *
     * @param id The id of the company user to be mark as non-verified.
     * @return View containing user profile. If the requester is not authenticated, redirects to home page.
     */
    @PostMapping("/admin/unverifyCompany")
    public String markCompanyAsNonVerified(@RequestParam("companyUserId") Long id) {
        try {
            if (!authService.doesUserHaveRole(Role.ADMIN)) {
                throw new IllegalStateException("You must be an admin to unmark a company as non-verified.");
            }
            Optional<AppUser> optionalAppUser = appUserService.getAppUser(id);

            if (optionalAppUser.isEmpty()) {
                return "redirect:/";
            }

            AppUser appUser = optionalAppUser.get();

            if (appUser.getRole() != Role.COMPANY) {
                throw new IllegalStateException("The user must be a verified company to be marked as a non-verified company.");
            }

            appUserService.markCompanyAsNonVerified(appUser);
            String userProfileURL = "redirect:/user/" + id;

            return userProfileURL;
        } catch (IllegalStateException e) {
            return "redirect:/";
        }
    }

    /**
     * Bans a user if the admin has determined they have sent a message
     * that is not allowed
     *
     * @param senderId The ID of the user to be banned
     * @return Chats page, so admin can continue monitoring chat reports
     */
    @PostMapping("/user/banUser")
    public String banUser(@RequestParam("senderId") Long senderId, @RequestParam("messageId") Long messageId) {
        if (!authService.doesUserHaveRole(Role.ADMIN)) {
            return "redirect:/";
        }

        boolean appUserBanned = appUserService.banUser(senderId);

        if (appUserBanned) {
            chatService.resolveReport(messageId);
        }

        return "redirect:/chats";
    }
}
