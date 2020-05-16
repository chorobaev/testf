package io.flaterlab.testf.spring;

import io.flaterlab.testf.persistence.dao.*;
import io.flaterlab.testf.persistence.model.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private TestRepository testRepository;
    private QuestionRepository questionRepository;

    public SetupDataLoader(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        RoleRepository roleRepository,
        PrivilegeRepository privilegeRepository,
        TestRepository testRepository,
        QuestionRepository questionRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
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

        // TODO: remove temporal data later
        // == create sample tests
        Test test1 = createTestIfNotFound("Sample test 1", user);
        Test test2 = createTestIfNotFound("Sample test 2", user);

        addQuestions(test2, 5);
        addQuestions(test1, 10);

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
                .title(title)
                .user(host)
                .slug(null)
                .summary("Lorem ipsum")
                .type(Test.Type.TEST)
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

    @Transactional
    public List<Question> addQuestions(Test test, int size) {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            questions.add(createQuestionIfNotFound("Question " + i, test));
        }
        return questions;
    }

    @Transactional
    public Question createQuestionIfNotFound(String content, Test test) {
        return questionRepository.findQuestionByContent(content).orElseGet(() ->
            questionRepository.save(
                Question.builder()
                    .test(test)
                    .type(Question.Type.SINGLE_CHOSE)
                    .active(true)
                    .level(5)
                    .score(1)
                    .createdAt(new Date())
                    .content(content)
                    .build()
            )
        );
    }
}