package io.flaterlab.testf.spring;

import io.flaterlab.testf.persistence.dao.PrivilegeRepository;
import io.flaterlab.testf.persistence.dao.RoleRepository;
import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.Privilege;
import io.flaterlab.testf.persistence.model.Role;
import io.flaterlab.testf.persistence.model.User;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private PasswordEncoder passwordEncoder;

    public SetupDataLoader(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PrivilegeRepository privilegeRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        createRoleIfNotFound("ROLE_USER", userPrivileges);

        // == create initial user
        createUserIfNotFound("user", "Test", "Test", "password",
            new ArrayList<>(Collections.singletonList(adminRole)));

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        return privilegeRepository.findByName(name).orElseGet(() -> privilegeRepository.save(new Privilege(name)));
    }

    @Transactional
    public Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name).orElseGet(() -> new Role(name));
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    public User createUserIfNotFound(
        final String username,
        final String firstName,
        final String lastName,
        final String password,
        final Collection<Role> roles
    ) {
        User user = userRepository.findByUsername(username).orElseGet(() -> User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .enabled(true)
            .passwordHash(passwordEncoder.encode(password))
            .build()
        );
        user.setRoles(roles);
        user.setRegisteredAt(new Date());
        user = userRepository.save(user);
        return user;
    }

}