package com.teafactory.pureleaf.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Component;

@Component
public class FirebaseUtil {

    public FirebaseToken verifyToken(String token) throws Exception {
        return FirebaseAuth.getInstance().verifyIdToken(token);
    }
}
