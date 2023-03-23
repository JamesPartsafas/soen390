package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This DefaultResume class serves as the entity to store the default resume of an app user.
 */
@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(nullable = true, columnDefinition="TEXT")
    private String defaultResume;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String fileName;


    /**
     * Default constructor
     */
    public Resume() {}

    /**
     * Create a new default resume given the app user id, the app user object, a resume and the file name of the resume.
     * @param id the app user id.
     * @param appUser the object representing the app user.
     * @param defaultResume the encoded resume of the default resume
     * @param fileName the file name of the resume
     */
    public Resume(Long id, AppUser appUser, String defaultResume, String fileName) {
        this.id = id;
        this.appUser = appUser;
        this.defaultResume = defaultResume;
        this.fileName = fileName;
    }

    /**
     * Create a new default resume given the app user object, a resume and the file name of the resume.
     * @param appUser the object representing the app user.
     * @param defaultResume the encoded resume for the default resume.
     * @param fileName the file name of the resume.
     */
    public Resume(AppUser appUser, String defaultResume, String fileName) {
        this.appUser = appUser;
        this.defaultResume = defaultResume;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getDefaultResume() {
        return defaultResume;
    }

    public void setDefaultResume(String defaultResume) {
        this.defaultResume = defaultResume;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DefaultResume{" +
                "id=" + id +
                ", appUser=" + appUser +
                ", defaultResume='" + defaultResume + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}