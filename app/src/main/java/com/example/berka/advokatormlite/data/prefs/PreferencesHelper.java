package com.example.berka.advokatormlite.data.prefs;

import javax.inject.Singleton;

/**
 * Created by berka on 01-Apr-18.
 */
@Singleton
public interface PreferencesHelper {

    String getCurrentUserName();

    void setUserNameAddressPlace(String userName, String address, String place);

    String getCurrentAddress();

    String getCurrentPlace();

}
