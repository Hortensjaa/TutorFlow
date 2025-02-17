package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;

public class Consts {
    private static String backendURL = "https://tutor-deployment-latest.onrender.com";
//    private static String frontendURL = "http://localhost:5173";
//    private static String frontendURL = "http://localhost:4173";
    private static String frontendURL = "https://tutor-flow-app.vercel.app";

    @JsonProperty("BackendUrl")
    public String getBackendURL() { return backendURL; }
    @JsonProperty("BackendUrl")
    public void setBackendURL(String value) { backendURL = value; }

    @JsonProperty("FrontendUrl")
    public static String getFrontendURL() { return frontendURL; }
    @JsonProperty("FrontendUrl")
    public void setFrontendURL(String value) { frontendURL = value; }
}
