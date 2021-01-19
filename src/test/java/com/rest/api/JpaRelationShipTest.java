package com.rest.api;


import com.rest.api.domain.Privilege;
import com.rest.api.domain.Role;
import com.rest.api.domain.User;
import com.rest.api.repository.PrivilegeRepository;
import com.rest.api.repository.RoleRepository;
import com.rest.api.repository.UserRepository;
import com.rest.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JpaRelationShipTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private Role role;
    private Privilege privilege;

    @Autowired
    private UserService userService;

    // tests

    @Test
    public void TEST_가자() {

        //userRepository.findByEmail("test@test.com");

        User.Request user = new User.Request();

        user.setEmail("wowo@test.com");
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword(passwordEncoder.encode("1234"));
        userService.singUp(user);


      /*  role = roleRepository.findByName("ROLE_USER");
        Privilege readPrivilege = privilegeRepository.findByName("READ_PRIVILEGE");
        Privilege writePrivilege = privilegeRepository.findByName("WRITE_PRIVILEGE");*/
/*

        roleRepository.findAll();

        privilegeRepository.findAll();

*/


        /*role = new Role("TEST_ROLE");
        roleRepository.save(role);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword(passwordEncoder.encode("123"));
        user.setEmail("john@doe.com");
        user.setRoles(Arrays.asList(role));
        user.setEnabled(true);
        userRepository.save(user);

        assertNotNull(userRepository.findByEmail(user.getEmail()));
        assertNotNull(roleRepository.findByName(role.getName()));
        user.setRoles(null);
        userRepository.delete(user);

        assertNull(userRepository.findByEmail(user.getEmail()));
        assertNotNull(roleRepository.findByName(role.getName()));*/
    }

}
