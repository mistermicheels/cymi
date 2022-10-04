package com.mistermicheels.cymi.web.api.controller.currentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mistermicheels.cymi.component.user.UserService;
import com.mistermicheels.cymi.component.user.entity.User;
import com.mistermicheels.cymi.config.security.CustomUserDetails;
import com.mistermicheels.cymi.web.api.output.ApiUser;

@RestController()
@RequestMapping("current-user")
public class CurrentUserController {

    private final UserService userService;

    @Autowired
    public CurrentUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ApiUser get(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = this.userService.findByIdOrThrow(userDetails.getId());
        return new ApiUser(user);
    }

}
