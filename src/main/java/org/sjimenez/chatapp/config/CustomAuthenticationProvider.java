package org.sjimenez.chatapp.config;

import org.sjimenez.chatapp.dao.ChatDao;
import org.sjimenez.chatapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private PasswordEncoder PASSWORD_ENCODER;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = chatDao.selectUserByMail(name);
        if (PASSWORD_ENCODER.matches(password, user.getNickname())) {
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
            return auth;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
