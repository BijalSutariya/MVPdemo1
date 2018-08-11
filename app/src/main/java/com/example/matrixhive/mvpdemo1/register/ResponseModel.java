package com.example.matrixhive.mvpdemo1.register;

public class ResponseModel {
    private String status;
    private DataBean data;
    private String message;

    public ResponseModel(String status, DataBean data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

        private String name;
        private String phone;
        private String email;
        private String device_token;
        private String device_type;
        private String referral;
        private String updated_at;
        private String created_at;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getReferral() {
            return referral;
        }

        public void setReferral(String referral) {
            this.referral = referral;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", ph_no='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", device_token='" + device_token + '\'' +
                    ", device_type='" + device_type + '\'' +
                    ", referral='" + referral + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
