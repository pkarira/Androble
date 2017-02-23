package com.mdg.androble.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham7stark on 24/2/17.
 */

public class BluetoothDeviceInfo implements Parcelable {
    String name;
    String address;

    protected BluetoothDeviceInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    protected BluetoothDeviceInfo(Parcel in) {
        name = in.readString();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BluetoothDeviceInfo> CREATOR = new Parcelable.Creator<BluetoothDeviceInfo>() {
        @Override
        public BluetoothDeviceInfo createFromParcel(Parcel in) {
            return new BluetoothDeviceInfo(in);
        }

        @Override
        public BluetoothDeviceInfo[] newArray(int size) {
            return new BluetoothDeviceInfo[size];
        }
    };
}
