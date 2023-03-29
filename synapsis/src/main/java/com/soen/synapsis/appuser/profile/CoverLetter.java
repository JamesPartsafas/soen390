package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This CoverLetter class serves as the entity to store the default cover letter of an app user.
 */
@Entity
public class CoverLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(nullable = true, columnDefinition="TEXT")
    private String defaultCoverLetter;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String fileName;

    /**
     * Default constructor
     */
    public CoverLetter() {}

    /**
     * Create a new default cover letter given the app user id, the app user object, a cover letter and the file name of the cover letter.
     * @param id the app user id.
     * @param appUser the object representing the app user.
     * @param defaultCoverLetter the encoded cover letter of the default cover letter.
     * @param fileName the file name of the cover letter.
     */
    public CoverLetter(Long id, AppUser appUser, String defaultCoverLetter, String fileName) {
        this.id = id;
        this.appUser = appUser;
        this.defaultCoverLetter = defaultCoverLetter;
        this.fileName = fileName;
    }

    /**
     * Create a new default cover letter given the app user object, a cover letter and the file name of the cover letter.
     * @param appUser the object representing the app user.
     * @param defaultCoverLetter the encoded cover letter for the default cover letter.
     * @param fileName the file name of the cover letter.
     */
    public CoverLetter(AppUser appUser, String defaultCoverLetter, String fileName) {
        this.appUser = appUser;
        this.defaultCoverLetter = defaultCoverLetter;
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

    public String getDefaultCoverLetter() {
        return defaultCoverLetter;
    }

    public void setDefaultCoverLetter(String defaultCoverLetter) {
        this.defaultCoverLetter = defaultCoverLetter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "CoverLetter{" +
                "id=" + id +
                ", appUser=" + appUser +
                ", defaultCoverLetter='" + defaultCoverLetter + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
