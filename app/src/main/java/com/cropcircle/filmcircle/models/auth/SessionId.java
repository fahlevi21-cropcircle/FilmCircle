package com.cropcircle.filmcircle.models.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionId {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("session_id")
    @Expose
    private String sessionId;

    /**
     * No args constructor for use in serialization
     *
     */
    public SessionId() {
    }

    /**
     *
     * @param success
     * @param sessionId
     */
    public SessionId(Boolean success, String sessionId) {
        super();
        this.success = success;
        this.sessionId = sessionId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
