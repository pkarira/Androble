package com.example.thispc.bluetooth_library2;

import java.util.Observable;

/**
 * Created by this pc on 17-09-2016.
 */
public class receivemsg extends Observable {
    String message = "";

    public void call(String s) {
        this.message = s;
        setChanged();
        notifyObservers(s);
    }

    public synchronized String getMessage() {
        return this.message;
    }
}
