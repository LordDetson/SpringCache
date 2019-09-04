package by.babanin.service;

import by.babanin.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface UserService {
    User create(User user);

    User create(String name, String email);

    User get(Integer id);

    List<User> getAll();

    @Cacheable(value = "users", key = "#user.name")
    User createOrReturnCached(User user);

    @CachePut(value = "users", key = "#user.name")
    User createAndRefreshCache(User user);

    void delete(Integer id);

    @CacheEvict("users")
    void deleteAndEvict(Integer id);
}
