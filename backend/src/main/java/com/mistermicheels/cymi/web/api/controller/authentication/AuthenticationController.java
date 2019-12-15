package com.mistermicheels.cymi.web.api.controller.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mistermicheels.cymi.component.user.LoginData;
import com.mistermicheels.cymi.component.user.SessionDataOutgoing;
import com.mistermicheels.cymi.component.user.UserService;
import com.mistermicheels.cymi.config.security.SecurityProperties;
import com.mistermicheels.cymi.web.api.controller.authentication.input.ConfirmEmailInput;
import com.mistermicheels.cymi.web.api.controller.authentication.input.LoginInput;
import com.mistermicheels.cymi.web.api.controller.authentication.input.SignupInput;
import com.mistermicheels.cymi.web.api.output.ApiSuccessResponse;
import com.mistermicheels.cymi.web.api.output.ApiUser;

@RestController()
@RequestMapping("authentication")
public class AuthenticationController {

    private final UserService userService;

    private final String sessionTokenCookieName;
    private final String csrfTokenCookieName;

    @Autowired
    public AuthenticationController(UserService userService,
            SecurityProperties securityProperties) {
        this.userService = userService;

        this.sessionTokenCookieName = securityProperties.getSessionTokenCookieName();
        this.csrfTokenCookieName = securityProperties.getCsrfTokenCookieName();
    }

    @PostMapping("sign-up")
    public ApiSuccessResponse signUp(@Valid @RequestBody SignupInput input) {
        LoginData loginData = new LoginData(input.email, input.password);
        this.userService.signUpUser(loginData, input.defaultDisplayName);
        return new ApiSuccessResponse();
    }

    @PostMapping("confirm-email")
    public ApiSuccessResponse confirmEmail(@Valid @RequestBody ConfirmEmailInput input) {
        this.userService.confirmEmail(input.token, input.userId);
        return new ApiSuccessResponse();
    }

    @PostMapping("log-in")
    public ApiUser logIn(@Valid @RequestBody LoginInput input, HttpServletResponse response) {
        LoginData loginData = new LoginData(input.email, input.password);
        SessionDataOutgoing sessionData = this.userService.getSessionDataForLogin(loginData);

        Cookie sessionTokenCookie = this.getSessionTokenCookie(sessionData.getSessionToken());
        response.addCookie(sessionTokenCookie);

        Cookie csrfTokenCookie = this.getCsrfTokenCookie(sessionData.getCsrfToken());
        response.addCookie(csrfTokenCookie);

        return new ApiUser(sessionData.getUser());
    }

    private Cookie getSessionTokenCookie(String value) {
        Cookie sessionTokenCookie = new Cookie(this.sessionTokenCookieName, value);
        sessionTokenCookie.setPath("/");
        sessionTokenCookie.setHttpOnly(true);
        return sessionTokenCookie;
    }

    private Cookie getCsrfTokenCookie(String value) {
        Cookie csrfTokenCookie = new Cookie(this.csrfTokenCookieName, value);
        csrfTokenCookie.setPath("/");
        return csrfTokenCookie;
    }

    @PostMapping("log-out")
    public ApiSuccessResponse logOut(HttpServletResponse response) {
        // cookies are deleted by setting value to null and Max-Age to 0
        // we must also set the other properties that we set when creating the cookie

        Cookie sessionTokenCookie = this.getSessionTokenCookie(null);
        sessionTokenCookie.setMaxAge(0);
        response.addCookie(sessionTokenCookie);

        Cookie csrfTokenCookie = this.getCsrfTokenCookie(null);
        sessionTokenCookie.setMaxAge(0);
        response.addCookie(csrfTokenCookie);

        return new ApiSuccessResponse();
    }

}
