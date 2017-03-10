package org.baoviet.spring;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.baoviet.persistence.dao.PrivilegeRepository;
import org.baoviet.persistence.dao.RoleRepository;
import org.baoviet.persistence.dao.UserRepository;
import org.baoviet.persistence.model.Privilege;
import org.baoviet.persistence.model.Role;
import org.baoviet.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege, passwordPrivilege));

        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setFirstName("trung");
        user.setLastName("vu dang");
        user.setPassword(passwordEncoder.encode("123qwe"));
        user.setEmail("123q@q.q");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);

        final Role userRole = roleRepository.findByName("ROLE_USER");
        User userTemp = new User();
        userTemp.setFirstName("user1");
        userTemp.setLastName("aladin");
        userTemp.setPassword(passwordEncoder.encode("123qwe"));
        userTemp.setEmail("234q@q.q");
        userTemp.setRoles(Arrays.asList(userRole));
        userTemp.setEnabled(true);
        userRepository.save(userTemp);

        userTemp = new User();
        userTemp.setFirstName("user2");
        userTemp.setLastName("aladin");
        userTemp.setPassword(passwordEncoder.encode("123qwe"));
        userTemp.setEmail("345q@q.q");
        userTemp.setRoles(Arrays.asList(userRole));
        userTemp.setEnabled(true);
        userRepository.save(userTemp);

        userTemp = new User();
        userTemp.setFirstName("user3");
        userTemp.setLastName("aladin");
        userTemp.setPassword(passwordEncoder.encode("123qwe"));
        userTemp.setEmail("456q@q.q");
        userTemp.setRoles(Arrays.asList(userRole));
        userTemp.setEnabled(true);
        userRepository.save(userTemp);

        userTemp = new User();
        userTemp.setFirstName("user4");
        userTemp.setLastName("aladin");
        userTemp.setPassword(passwordEncoder.encode("123qwe"));
        userTemp.setEmail("567q@q.q");
        userTemp.setRoles(Arrays.asList(userRole));
        userTemp.setEnabled(true);
        userRepository.save(userTemp);

        userTemp = new User();
        userTemp.setFirstName("user5");
        userTemp.setLastName("aladin");
        userTemp.setPassword(passwordEncoder.encode("123qwe"));
        userTemp.setEmail("678q@q.q");
        userTemp.setRoles(Arrays.asList(userRole));
        userTemp.setEnabled(true);
        userRepository.save(userTemp);

        alreadySetup = true;
    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}