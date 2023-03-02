package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

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

    public ProfilePicture() {
    }

    public ProfilePicture(Long id, AppUser appUser, String image) {
        this.id = id;
        this.appUser = appUser;
        this.image = image;
    }

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
