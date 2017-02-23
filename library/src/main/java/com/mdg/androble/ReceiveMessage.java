package com.mdg.androble;

import java.util.Observable;

/**
 * Created by this pc on 17-09-2016.
 */
public class ReceiveMessage extends Observable {
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
