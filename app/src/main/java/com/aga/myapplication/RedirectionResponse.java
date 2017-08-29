package com.aga.myapplication;

/**
 * Created by A599885 on 25/07/2017.
 */
public class RedirectionResponse {

    private String publicKeyValue;
    private String redirectionData;
    private int redirectionStatusCode;
    private String redirectionStatusMessage;
    private String redirectionUrl;
    private String redirectionVersion;
    private String seal;

    public String getRedirectionData() {
        return redirectionData;
    }

    public void setRedirectionData(String redirectionData) {
        this.redirectionData = redirectionData;
    }

    public int getRedirectionStatusCode() {
        return redirectionStatusCode;
    }

    public void setRedirectionStatusCode(int redirectionStatusCode) {
        this.redirectionStatusCode = redirectionStatusCode;
    }

    public String getRedirectionStatusMessage() {
        return redirectionStatusMessage;
    }

    public void setRedirectionStatusMessage(String redirectionStatusMessage) {
        this.redirectionStatusMessage = redirectionStatusMessage;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getRedirectionVersion() {
        return redirectionVersion;
    }

    public void setRedirectionVersion(String redirectionVersion) {
        this.redirectionVersion = redirectionVersion;
    }

    public String getSeal() {
        return seal;
    }

    public void setSeal(String seal) {
        this.seal = seal;
    }

    public String getPublicKeyValue() {
        return publicKeyValue;
    }

    public void setPublicKeyValue(String publicKeyValue) {
        this.publicKeyValue = publicKeyValue;
    }
}
