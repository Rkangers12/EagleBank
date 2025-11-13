package com.studio.eaglebank.testdata;

import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;

import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;

public class AuthTestDataHelper {

    public static UserAuthRequest getUserAuthRequestData(String password, String userId) {
        return new UserAuthRequest(userId, password);
    }

    public static UserAuthResponse getUserAuthResponse() {
        return new UserAuthResponse(USER_ID, "amelia.thompson@example.com", "jwtToken-random-token");
    }
}
