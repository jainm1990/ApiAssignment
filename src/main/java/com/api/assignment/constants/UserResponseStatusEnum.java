package com.api.assignment.constants;

public enum UserResponseStatusEnum {


        INVAID_REQUEST_PARAMETERS(02,"Request Parameters Invalid", ApplicationConstant.FAILED),
        HTTP_METHOD_NOT_ALLOWED(02,"HTTP METHOD NOT ALLOWED", ApplicationConstant.FAILED),
        REQUEST_PROCESSED_SUCCESS(01, "User Registration Success", "OK"),
        REQUEST_FAILED(03, "User Registration Failed",ApplicationConstant.FAILED),
        REQUEST_FAILED_DUPLICATE_RECORD(03, "User Email Address Already Exists",ApplicationConstant.FAILED),
        ERROR_UPLOADING_IMAGE(03,"Error While Uploading Image", ApplicationConstant.FAILED),
    IMAGE_UPLOAD_SUCCESS(01,"Image Upload Success", "OK"),
    IMAGE_VIEW_SUCCESS(01,"Image View Success", "OK"),
    DELETE_IMAGE_SUCCESS(01,"Delete Image Success", "OK"),
    DELETE_IMAGE_FAILED(03,"Delete Image Failed", ApplicationConstant.FAILED);




    private Integer messageCode;
        private String description;
        private String status;

        private UserResponseStatusEnum(Integer messageCode, String description, String status) {
            this.messageCode = messageCode;
            this.description = description;
            this.status = status;
        }

        public Integer getMessageCode() {
            return messageCode;
        }

        public void setMessageCode(Integer messageCode) {
            this.messageCode = messageCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {     return status;  }

}
