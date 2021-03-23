package com.rest.api.config;


import com.rest.api.domain.*;
import com.rest.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BoardQueryRepository boardQueryRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;


    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        //final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege));
        //final UserRoles adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        //final UserRoles userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);


        UserRoles adminRole = new UserRoles();
        Role r = createRoleIfNotFound("ROLE_ADMIN");
        adminRole.setRole(r);

        UserRoles userRole = new UserRoles();
        Role u = createRoleIfNotFound("ROLE_USER");
        userRole.setRole(u);

        // == create initial user
        User admin = createUserIfNotFound("adsfdas@naver.com", "adsfdas", "admin", "1234", new ArrayList<UserRoles>(Arrays.asList(adminRole)));
        User user = createUserIfNotFound("byungki9770@gmail.com", "byungki9770", "user", "1234", new ArrayList<>(Arrays.asList(userRole)));

        adminRole.setUser(admin);
        userRole.setUser(user);

        createUserRolefNotFound(adminRole);
        createUserRolefNotFound(userRole);

       /* for(int i = 0; i < 100; i++){
            boardQueryRepository.save(Board.builder().title("title " + i).content("content- " +i).build());
        }*/

        alreadySetup = true;
    }

    @Transactional
    void createUserRolefNotFound(UserRoles u) {
        userRoleRepository.save(u);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }


    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        //role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Collection<UserRoles> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setMailCertification(true);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

}