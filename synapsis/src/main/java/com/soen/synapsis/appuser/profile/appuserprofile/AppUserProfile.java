package com.soen.synapsis.appuser.profile.appuserprofile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

@Entity
public class AppUserProfile {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public AppUserProfile() {
    }

    public AppUserProfile(AppUser appUser, Long id, String education, String skill, String work, String course,
                          String phone, String volunteering, String project, String award, String language) {
        this.appUser = appUser;
        this.id = id;
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

    public AppUserProfile(AppUser appUser, String education, String skill, String work, String course,
                          String phone, String volunteering, String project, String award, String language) {

        this.appUser = appUser;
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
