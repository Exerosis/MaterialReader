package me.exerosis.materialreader.model;

import android.os.Parcel;

import me.exerosis.modelable.Model;
import me.exerosis.modelable.Modelable;

public class Source implements Modelable {
    @Override
    public void writeToModel(Model out) {

    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static Modelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromModel(Model in) {
            return null;
        }

        @Override
        public Object createFromParcel(Parcel in) {
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };
}
