package com.comp2100.surveymonster.DateStorage;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * user class, enable question to add user
 */
// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
// [END blog_user_class]
