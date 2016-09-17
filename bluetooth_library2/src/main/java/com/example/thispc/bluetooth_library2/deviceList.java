package com.example.thispc.bluetooth_library2;

import java.util.Observable;

/**
 * Created by this pc on 17-09-2016.
 */
public class deviceList extends Observable {
    String s1;
    public void call(String s)
    {
        this.s1=s;
        setChanged();
        notifyObservers(s);
    }
    public synchronized String getContent() {
        return s1;
    }
}
