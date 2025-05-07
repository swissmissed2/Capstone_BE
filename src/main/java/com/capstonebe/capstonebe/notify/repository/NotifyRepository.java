package com.capstonebe.capstonebe.notify.repository;

import com.capstonebe.capstonebe.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {

    List<Notify> findByCreatedAtBefore(LocalDateTime dateTime);
}
