package com.example.myapplication.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StudentData implements Parcelable {
    private String name;
    private int score;

    public StudentData(String name, int score) {
        this.name = name;
        this.score = score;
    }

    protected StudentData(Parcel in) {
        name = in.readString();
        score = in.readInt();
    }

    public static final Creator<StudentData> CREATOR = new Creator<StudentData>() {
        @Override
        public StudentData createFromParcel(Parcel in) {
            return new StudentData(in);
        }

        @Override
        public StudentData[] newArray(int size) {
            return new StudentData[size];
        }
    };

    public String getName() { return name; }
    public int getScore() { return score; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(score);
    }
}
