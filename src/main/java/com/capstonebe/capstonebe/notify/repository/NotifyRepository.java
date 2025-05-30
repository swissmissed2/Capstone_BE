package com.capstonebe.capstonebe.notify.repository;

import com.capstonebe.capstonebe.notify.entity.Notify;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotifyRepository extends JpaRepository<Notify, Long> {

    List<Notify> findByCreatedAtBefore(LocalDateTime dateTime);

    Page<Notify> findByReceiverAndType(User receiver, NotifyType notifyType, Pageable pageable);

    Optional<Notify> findByReceiverAndId(User receiver, Long id);
}
