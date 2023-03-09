package com.soen.synapsis.appuser.profile.appuserprofile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This AppUserProfile class serves as the entity to store the profile of an app user.
 */
@Entity
public class AppUserProfile {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, columnDefinition="TEXT")
    private String description;

    @Column(nullable = true)
    private String education;

    @Column(nullable = true)
    private String skill;

    @Column(nullable = true)
    private String work;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String volunteering;

    @Column(nullable = true)
    private String course;

    @Column(nullable = true)
    private String project;

    @Column(nullable = true)
    private String award;

    @Column(nullable = true)
    private String language;

    /**
     * Empty constructor.
     */
    public AppUserProfile() {
    }

    /**
     * Create a new app user profile given an app user object, id, description, education, skill, work, course, phone, volunteering, project, award and language.
     * @param appUser the object representing an app user.
     * @param id the app user's id.
     * @param description the app user's description on his profile.
     * @param education the app user's education on his profile.
     * @param skill the app user's skill on his profile.
     * @param work the app user's work on his profile.
     * @param course the app user's course on his profile.
     * @param phone the app user's phone on his profile.
     * @param volunteering the app user's volunteering on his profile.
     * @param project the app user's project on his profile.
     * @param award the app user's award on his profile.
     * @param language the app user's language on his profile.
     */
    public AppUserProfile(AppUser appUser, Long id, String description, String education, String skill, String work, String course,
                          String phone, String volunteering, String project, String award, String language) {
        this.appUser = appUser;
        this.id = id;
        this.description = description;
        this.education = education;
        this.skill = skill;
        this.work = work;
        this.course = course;
        this.phone = phone;
        this.volunteering = volunteering;
        this.project = project;
        this.award = award;
        this.language = language;
    }

    /**
     * Create a new app user profile given an app user object, description, education, skill, work, course, phone, volunteering, project, award and language.
     * @param appUser the object representing an app user.
     * @param description the app user's description on his profile.
     * @param education the app user's education on his profile.
     * @param skill the app user's skill on his profile.
     * @param work the app user's work on his profile.
     * @param course the app user's course on his profile.
     * @param phone the app user's phone on his profile.
     * @param volunteering the app user's volunteering on his profile.
     * @param project the app user's project on his profile.
     * @param award the app user's award on his profile.
     * @param language the app user's language on his profile.
     */
    public AppUserProfile(AppUser appUser, String description, String education, String skill, String work, String course,
                          String phone, String volunteering, String project, String award, String language) {

        this.appUser = appUser;
        this.description = description;
        this.education = education;
        this.skill = skill;
        this.work = work;
        this.course = course;
        this.phone = phone;
        this.volunteering = volunteering;
        this.project = project;
        this.award = award;
        this.language = language;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVolunteering() {
        return volunteering;
    }

    public void setVolunteering(String volunteering) {
        this.volunteering = volunteering;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "AppUserProfile{" +
                "id=" + id +
                ", education='" + education + '\'' +
                ", skill='" + skill + '\'' +
                ", work='" + work + '\'' +
                ", phone='" + phone + '\'' +
                ", course='" + course + '\'' +
                ", project='" + project + '\'' +
                ", volunteering='" + volunteering + '\'' +
                ", award='" + award + '\'' +
                ", language=" + language +
                '}';
    }
}
