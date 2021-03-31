package com.rest.api.config;


import com.rest.api.domain.*;
import com.rest.api.repository.*;
import com.rest.api.service.UserService;
import com.rest.api.util.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
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
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PrivilegesRepository privilegesRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private BoardQueryRepository boardQueryRepository;

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
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege));

        UserRoles adminRole = new UserRoles();
        Role a = createRoleIfNotFound("ROLE_ADMIN");
        adminRole.setRole(a);

        UserRoles userRole = new UserRoles();
        Role u = createRoleIfNotFound("ROLE_USER");
        userRole.setRole(u);

        // == create initial user
        User admin = createUserIfNotFound("test@naver.com", "test", "admin", "1234", new ArrayList<UserRoles>(Arrays.asList(adminRole)));
        User user = createUserIfNotFound("test0@gmail.com", "test", "user", "1234", new ArrayList<>(Arrays.asList(userRole)));

        adminRole.setUser(admin);
        userRole.setUser(user);

        // == create initial  UserRoles
        createUserRolefNotFound(adminRole);
        createUserRolefNotFound(userRole);

        // == create initial  privileges
        createPrivilegesIfNotFound(adminPrivileges , a);
        createPrivilegesIfNotFound(userPrivileges , u);

        User test = userService.findByEmail("test@naver.com");

        for(int i = 0; i < 3; i++){
            Board board = new Board();
            board.setUser(test);
            board.setTitle("title " + i);
            board.setContent("content " + i);
            boardQueryRepository.save(board);
        }
        PageRequest pageRequest = new PageRequest();
        Page<Board> board = boardQueryRepository.getList(pageRequest.of() , "" , "");
        board.forEach((k) ->{
            Comment c = new Comment();
            c.setUser(test);
            c.setBoard(k);
            c.setTitle("댓글제목 - 게시글번호는 :  " + k.getId());
            c.setContent("댓글내용  - 게시글번호는 :" + k.getId());
            boardQueryRepository.commentSave(c);
        });

        alreadySetup = true;
    }


    @Transactional
    void createPrivilegesIfNotFound(List<Privilege> p, Role u ) {
        for(int i = 0; i < p.size(); i++){
            Privileges privileges = new Privileges();
            privileges.setRole(u);
            privileges.setPrivilege(p.get(i));
            privilegesRepository.save(privileges);
        }
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
    void createUserRolefNotFound(UserRoles u) {
        userRoleRepository.save(u);
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