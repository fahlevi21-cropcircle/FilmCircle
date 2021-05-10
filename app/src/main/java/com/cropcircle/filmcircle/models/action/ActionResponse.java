package com.cropcircle.filmcircle.models.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    /**
     * No args constructor for use in serialization
     *
     */
    public ActionResponse() {
    }

    /**
     *
     * @param success
     * @param statusMessage
     * @param statusCode
     */
    public ActionResponse(Boolean success, Integer statusCode, String statusMessage) {
        super();
        this.success = success;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
