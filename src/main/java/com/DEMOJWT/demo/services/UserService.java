package com.DEMOJWT.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    /**
     * If the user exists, and the password is correct, return a JWT token
     *
     * @param username The username of the user
     * @param pwd the password of the user
     * @return A JWT token.
     */
    public String login(String username, String pwd) {
        User user = repository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPwd().equals(pwd)) {
            return getJWTToken(user.getUsername());
        }
        return new RuntimeException("Invalid username or password").toString();
    }

    /**
     * If the user exists, return "User already exist". If the user doesn't exist, create a new user
     * and return a JWT token
     *
     * @param username The username of the user
     * @param pwd the password of the user
     * @return A JWT token
     */
    public String registerUser(String username, String pwd) {
        Optional<User> userExist = repository.findByUsername(username);
        if (userExist.isPresent()) {
            return "User already exist";
        }
        User user = new User();
        user.setUsername(username);
        user.setPwd(pwd);
        repository.save(user);

        return getJWTToken(user.getUsername());
    }

    /**
     * It creates a JWT token with the username as the subject, the current time as the issued at time,
     * and the current time + 10 minutes as the expiration time
     *
     * @param username The username of the user.
     * @return A JWT token
     */
    public String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sofkaJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes())
                .compact();

        return "Valido " + token;
    }
}
