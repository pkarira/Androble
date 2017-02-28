package com.mdg.androble.listeners;

/**
 * @author Deepankar Agrawal
 */

public interface ConnectionStatusListener {

    /**
     * called whenever a new user is connected
     *
     * @param id id of the user
     */
    void onConnected(int id);

    /**
     * called whenever existing user is disconnected
     *
     * @param id id of the user
     */
    void onDisconnected(int id);
}
