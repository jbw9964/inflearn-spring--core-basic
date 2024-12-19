package sections.section03_04.service;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import sections.section03_04.*;
import sections.section03_04.Run.*;
import sections.section03_04.domain.*;
import sections.section03_04.repository.*;


class UserServiceTest {

    UserRepository userRepo;
    UserService userService;

    User testUser;
    final int TEST_SIZE = 10;

    @BeforeEach
    void setUp() {
        Config config = Run.init();

        userRepo = config.get(UserRepository.class);
        userService = config.get(UserService.class);

        testUser = new User();
        testUser.setRole(UserRole.NORMAL);
        userRepo.save(testUser);
    }

    @Test
    void findById() {
        Long id = testUser.getId();

        User result = userService.findById(id).orElseThrow();

        assertThat(result).satisfies(
                r -> assertThat(r.getId()).isEqualTo(id),
                r -> assertThat(r.getRole()).isEqualTo(testUser.getRole())
        );
    }

    @Test
    void signUp() {
        User newTestUser = new User();
        newTestUser.setRole(UserRole.NORMAL);

        User result = userService.signUp(newTestUser);

        assertThat(result).satisfies(
                r -> assertThat(r.getRole()).isEqualTo(newTestUser.getRole()),
                r -> assertThat(r.getId()).isNotNull().isEqualTo(newTestUser.getId())
        );
    }

    @Test
    void getAllUsers() {
        List<User> testUserList = IntStream.range(0, TEST_SIZE).boxed()
                .map(i -> new User())
                .peek(u -> u.setRole(UserRole.NORMAL))
                .map(userService::signUp).toList();

        List<User> result = userService.getAllUsers()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();

        assertThat(result).hasSize(testUserList.size() + 1);
        assertThat(result).containsAll(testUserList);
    }

    @Test
    void deleteUser() {
        Long id = testUser.getId();
        userService.deleteUser(id);

        assertThat(userRepo.findById(id)).isEmpty();
    }

    @Test
    void editUser() {
        testUser.setRole(UserRole.VIP);

        Long id = testUser.getId();
        User result = userService.editUser(testUser);

        assertThat(result).satisfies(
                r -> assertThat(r.getId()).isEqualTo(id),
                r -> assertThat(r.getRole()).isEqualTo(testUser.getRole())
        );
    }
}