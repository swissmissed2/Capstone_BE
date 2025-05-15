package com.capstonebe.capstonebe.qr.repository;

import com.capstonebe.capstonebe.qr.entity.Qr;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QrRepository extends JpaRepository<Qr, String> {

}
