package com.capstonebe.capstonebe.qr.repository;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.qr.entity.Qr;
import com.capstonebe.capstonebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QrRepository extends JpaRepository<Qr, String> {

    Optional<Qr> findByUserAndItem(User user, Item item);
}
