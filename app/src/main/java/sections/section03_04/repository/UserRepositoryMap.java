package sections.section03_04.repository;

import java.util.*;
import sections.section03_04.domain.*;
import sections.section03_04.exception.*;

public class UserRepositoryMap implements UserRepository {

    private static final Map<Long, User> users = new HashMap<>();
    private static long ID_COUNT = 1;

    public UserRepositoryMap() {
        ID_COUNT = 1;
        users.clear();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(
                users.get(id)
        );
    }

    @Override
    public User save(User user) {
        if (!user.isValidWithoutId()) {
            throw new InvalidUserException();
        }

        user.setId(ID_COUNT);
        users.put(ID_COUNT++, user);

        return user;
    }

    @Override
    public User update(User user) {
        if (!user.isValid()) {
            throw new InvalidUserException();
        }

        Long id = user.getId();
        if (this.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        }

        users.put(id, user);

        return user;
    }

    @Override
    public void deleteById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException();
        }

        users.remove(id);
    }


    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }
}
