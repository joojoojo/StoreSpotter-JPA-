package com.sojoo.StoreSpotter.repository.user;

import com.sojoo.StoreSpotter.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    List<User> findByNicknameAndPhone(String username, String userPhone);

    Optional<User> findByUsername(String username);
}