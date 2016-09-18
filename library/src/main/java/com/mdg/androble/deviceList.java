package com.mdg.androble;

import java.util.Observable;

/**
 * Created by this pc on 17-09-2016.
 */
public class DeviceList extends Observable {

    String s1;

    void call(String s) {
        this.s1=s;
        setChanged();
        notifyObservers(s);
    }

    synchronized String getContent() {
        return s1;
    }
}
