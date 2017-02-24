package com.mdg.androble.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shubham7stark on 24/2/17.
 */

public class BluetoothDeviceInfo implements Parcelable {

    private String name;
    private String address;

    public BluetoothDeviceInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    private BluetoothDeviceInfo(Parcel in) {
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
