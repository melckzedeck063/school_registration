package com.example.school_registration.dto;

public class ResponseMessage {
    private String request_id;
    private Integer code;
    private String message;
    private Boolean successful;
    private Integer valid;
    private Integer invalid;
    private Integer duplicates;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    public Integer getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(Integer duplicates) {
        this.duplicates = duplicates;
    }
}
