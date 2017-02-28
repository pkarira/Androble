package com.mdg.androble.utils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Deepankar Agrawal
 */

public class UuidGenerator {

    private static ArrayList<UUID> mUuids;

    public static ArrayList<UUID> generateUUIDs(){

        if(mUuids == null){

            mUuids = new ArrayList<>(4);

            // generate unique uuids for the session
            mUuids.add(UUID.fromString("b7746a40-c758-4868-aa19-7ac6b3475dfc"));
            mUuids.add(UUID.fromString("2d64189d-5a2c-4511-a074-77f199fd0834"));
            mUuids.add(UUID.fromString("e442e09a-51f3-4a7b-91cb-f638491d1412"));
            mUuids.add(UUID.fromString("a81d6504-4536-49ee-a475-7d96d09439e4"));
        }
        return mUuids;
    }
}
