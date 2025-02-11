package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;

public class Consts {
    private String[] subject;
    private String[] primarySchool;
    private String[] highSchool;
    private String backendURL;
    private String frontendURL;

    @JsonProperty("Subject")
    public String[] getSubject() { return subject; }
    @JsonProperty("Subject")
    public void setSubject(String[] value) { this.subject = value; }

    @JsonProperty("PrimarySchool")
    public String[] getPrimarySchool() { return primarySchool; }
    @JsonProperty("PrimarySchool")
    public void setPrimarySchool(String[] value) { this.primarySchool = value; }

    @JsonProperty("HighSchool")
    public String[] getHighSchool() { return highSchool; }
    @JsonProperty("HighSchool")
    public void setHighSchool(String[] value) { this.highSchool = value; }

    @JsonProperty("BackendUrl")
    public String getBackendURL() { return backendURL; }
    @JsonProperty("BackendUrl")
    public void setBackendURL(String value) { this.backendURL = value; }

    @JsonProperty("FrontendUrl")
    public String getFrontendURL() { return frontendURL; }
    @JsonProperty("FrontendUrl")
    public void setFrontendURL(String value) { this.frontendURL = value; }
}
