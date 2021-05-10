package com.cropcircle.filmcircle.models.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestToken {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("request_token")
    @Expose
    private String requestToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public RequestToken() {
    }

    /**
     *
     * @param success
     * @param requestToken
     * @param expiresAt
     */
    public RequestToken(Boolean success, String expiresAt, String requestToken) {
        super();
        this.success = success;
        this.expiresAt = expiresAt;
        this.requestToken = requestToken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
