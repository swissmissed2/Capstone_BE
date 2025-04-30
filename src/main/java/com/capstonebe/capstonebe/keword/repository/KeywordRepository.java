package com.capstonebe.capstonebe.keword.repository;

import com.capstonebe.capstonebe.keword.entity.Keyword;
import com.capstonebe.capstonebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    boolean existsByUserAndKeyword(User user, String keyword);

    List<Keyword> findByUser(User user);

    Optional<Keyword> findByUserAndId(User user, Long id);

}
