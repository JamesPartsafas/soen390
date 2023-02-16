package com.soen.synapsis.appuser.profile.appuserprofile.updateprofile;

public class UpdateAppUserProfileRequest {
    private String education;
    private String skill;
    private String work;
    private String phone;
    private String volunteering;
    private String course;
    private String project;
    private String award;
    private String language;

    public UpdateAppUserProfileRequest() {
    }

    public UpdateAppUserProfileRequest(String education, String skill, String work, String phone, String volunteering, String course, String project, String award, String language) {
        this.education = education;
        this.skill = skill;
        this.work = work;
        this.phone = phone;
        this.volunteering = volunteering;
        this.course = course;
        this.project = project;
        this.award = award;
        this.language = language;
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

    public String getCourse() {

        return course;
    }

    public void setCourse(String course) {

        this.course = course;
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
        return "UpdateAppUserProfileRequest{" +
                "education='" + education + '\'' +
                ", skill='" + skill + '\'' +
                ", work='" + work + '\'' +
                ", phone='" + phone + '\'' +
                ", volunteering='" + volunteering + '\'' +
                ", course='" + course + '\'' +
                ", project='" + project + '\'' +
                ", award='" + award + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
