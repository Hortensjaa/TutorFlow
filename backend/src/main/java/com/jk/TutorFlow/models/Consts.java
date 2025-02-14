package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;

public class Consts {
    private static String backendURL = "http://localhost:8080";
    private static String frontendURL = "http://localhost:5173";

    @JsonProperty("BackendUrl")
    public String getBackendURL() { return backendURL; }
    @JsonProperty("BackendUrl")
    public void setBackendURL(String value) { backendURL = value; }

    @JsonProperty("FrontendUrl")
    public static String getFrontendURL() { return frontendURL; }
    @JsonProperty("FrontendUrl")
    public void setFrontendURL(String value) { frontendURL = value; }
}
