package com.ferum_bot.springjwt.services;

import com.ferum_bot.springjwt.models.entities.Role;
import com.ferum_bot.springjwt.models.entities.User;
import com.ferum_bot.springjwt.repositories.RoleRepository;
import com.ferum_bot.springjwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class MainServiceImpl implements MainService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainServiceImpl(
        UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(String userNickname) {
        return userRepository.findUserByNickname(userNickname);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String userNickname, String roleName) {
        var user = userRepository.findUserByNickname(userNickname);
        var role = roleRepository.findRoleByName(roleName);
        user.addRole(role);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userNickname) throws UsernameNotFoundException {
        var user = userRepository.findUserByNickname(userNickname);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userNickname);
        }

        var nickname = user.getNickname();
        var password = user.getPassword();
        var authorities = getUserAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
            nickname, password, authorities
        );
    }

    private static Collection<SimpleGrantedAuthority> getUserAuthorities(User user) {
        var resultAuthorities = new ArrayList<SimpleGrantedAuthority>();
        user.getRoles().forEach(role -> {
            var authority = new SimpleGrantedAuthority(role.getName());
            resultAuthorities.add(authority);
        });
        return resultAuthorities;
    }
}
