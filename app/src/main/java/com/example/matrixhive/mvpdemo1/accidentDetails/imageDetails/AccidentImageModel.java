package com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.stream.Stream;

public class AccidentImageModel extends ArrayList<Parcelable> implements Parcelable{

    private String images;

    private void readIn(Parcel in) {
        images = in.readString();
    }

    public static final Creator<AccidentImageModel> CREATOR = new Creator<AccidentImageModel>() {
        @Override
        public AccidentImageModel createFromParcel(Parcel in) {
            AccidentImageModel model = new AccidentImageModel();
            model.readIn(in);
            return model;
        }

        @Override
        public AccidentImageModel[] newArray(int size) {
            return new AccidentImageModel[size];
        }
    };

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "AccidentImageModel{" +
                "images='" + images + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(images);
    }

    @Override
    public Stream<Parcelable> stream() {
        return null;
    }
}
