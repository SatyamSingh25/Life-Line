package com.hu22.bloodBankBackendPrivate.services;

import com.hu22.bloodBankBackendPrivate.dto.JwtRequest;
import com.hu22.bloodBankBackendPrivate.dto.JwtResponse;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> createJwtToken(JwtRequest jwtRequest) throws Exception{
        String userEmail = jwtRequest.getEmail();
        String userPassword = jwtRequest.getUserPassword();


        authenticate(userEmail, userPassword); //it throw an exception

        final UserDetails userDetails = loadUserByUsername(userEmail);

        //generating token
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userRepository.findById(userEmail).get();
        return new ResponseEntity<>(new JwtResponse(user, newGeneratedToken), HttpStatus.ACCEPTED);
//        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(userName).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    getAuthroties(user)
            );
        }
        else{
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set getAuthroties(User user){
        Set authorities = new HashSet();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });

        return authorities;
    }

    private void authenticate(String userEmail, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, userPassword)); //there may be two exception (disabled, bad credentiles) exceptions
        }catch(DisabledException e){
            throw new Exception("User is disabled");
        }catch (BadCredentialsException e){
            throw new Exception("Bad credentials from user");
        }
    }
}
