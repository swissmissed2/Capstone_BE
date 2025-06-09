package com.capstonebe.capstonebe.matching.repository;

import com.capstonebe.capstonebe.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByLostItemIdAndFoundItemId(Long lostItemId, Long foundItemId);
    List<Matching> findAllByLostItemIdOrFoundItemId(Long lostItemId, Long foundItemId);
}
