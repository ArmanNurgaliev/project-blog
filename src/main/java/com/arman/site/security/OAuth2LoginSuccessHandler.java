package com.arman.site.security;

import com.arman.site.models.AuthenticationProvider;
import com.arman.site.models.CustomOAuth2User;
import com.arman.site.models.User;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        User userFromDB = userService.getUserByEmail(email);
        String username = oAuth2User.getName();

        if (userFromDB == null) {
            userService.createNewUserAfterOAuth2Login(email, username, AuthenticationProvider.GOOGLE);
        }
        else {
            userService.updateUserAfterOAuth2Login(userFromDB, username, AuthenticationProvider.GOOGLE);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
