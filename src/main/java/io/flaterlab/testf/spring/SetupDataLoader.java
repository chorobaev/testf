package io.flaterlab.testf.spring;

import io.flaterlab.testf.persistence.dao.PrivilegeRepository;
import io.flaterlab.testf.persistence.dao.RoleRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.Privilege;
import io.flaterlab.testf.persistence.model.Role;
import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.persistence.model.User;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private TestRepository testRepository;
    private PasswordEncoder passwordEncoder;

    public SetupDataLoader(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PrivilegeRepository privilegeRepository,
        TestRepository testRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.testRepository = testRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege takeTestPrivilege = createPrivilegeIfNotFound("TAKE_PRIVILEGE");
        final Privilege createTestPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        final Privilege manageAccountsPrivilege = createPrivilegeIfNotFound("MANAGE_ACCOUNTS_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(
            passwordPrivilege,
            readPrivilege,
            takeTestPrivilege,
            createTestPrivilege,
            manageAccountsPrivilege)
        );
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(
            passwordPrivilege,
            readPrivilege,
            takeTestPrivilege
        ));
        final List<Privilege> hostPrivileges = new ArrayList<>(Arrays.asList(
            passwordPrivilege,
            readPrivilege,
            takeTestPrivilege,
            createTestPrivilege
        ));

        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        createRoleIfNotFound("ROLE_USER", userPrivileges);
        createRoleIfNotFound("ROLE_HOST", hostPrivileges);

        // == create initial user
        User user = createUserIfNotFound("admin", "Super", "Admin", "superPassword",
            new ArrayList<>(Collections.singletonList(adminRole)));

        createTestIfNotFound("Sample test 1", user);
        createTestIfNotFound("Sample test 2", user);

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

    @Transactional
    public Test createTestIfNotFound(String title, User host) {
        Date now = new Date();
        return testRepository.findTestByTitle(title).orElseGet(() ->
            testRepository.save(Test.builder()
                .user(host)
                .title(title)
                .slug(null)
                .summary("Lorem ipsum")
                .type("TEST")
                .score(100)
                .published(true)
                .createdAt(now)
                .startsAt(now)
                .endsAt(new Date(now.getTime() + 36000000))
                .content(null)
                .build()
            )
        );
    }
}