package com.example.matrixhive.mvpdemo1.register;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestModel implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String ph_no;
    private String device_token;
    private String device_type;

   /* public RequestModel(String name, String email, String password, String ph_no, String device_token, String device_type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ph_no = ph_no;
        this.device_token = device_token;
        this.device_type = device_type;
    }*/

    private void readIn(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        ph_no = in.readString();
        device_token = in.readString();
        device_type = in.readString();
    }

    public static final Creator<RequestModel> CREATOR = new Creator<RequestModel>() {
        @Override
        public RequestModel createFromParcel(Parcel in) {
            RequestModel model = new RequestModel();
            model.readIn(in);
            return model;
        }

        @Override
        public RequestModel[] newArray(int size) {
            return new RequestModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPh_no() {
        return ph_no;
    }

    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(ph_no);
        dest.writeString(device_token);
        dest.writeString(device_type);
    }
}
