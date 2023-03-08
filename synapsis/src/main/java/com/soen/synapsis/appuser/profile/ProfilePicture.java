package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This ProfilePicture class serves as the entity to store the profile picture of an app user.
 */
@Entity
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(nullable = true, columnDefinition="TEXT")
    private String image;

    /**
     * Empty constructor.
     */
    public ProfilePicture() {
    }

    /**
     * Create a new profile picture given the id, app user object and image.
     * @param id the app user's id.
     * @param appUser the object representing the app user.
     * @param image the encoded image for the profile picture.
     */
    public ProfilePicture(Long id, AppUser appUser, String image) {
        this.id = id;
        this.appUser = appUser;
        this.image = image;
    }

    /**
     * Create a new profile picture given the app user object and image.
     * @param appUser the object representing the app user.
     * @param image the encoded image for the profile picture.
     */
    public ProfilePicture(AppUser appUser, String image) {
        this.appUser = appUser;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
                "id=" + id +
                ", appUser=" + appUser +
                ", image='" + image + '\'' +
                '}';
    }
}
