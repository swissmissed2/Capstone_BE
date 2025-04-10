package com.capstonebe.capstonebe.inquiry.repository;

import com.capstonebe.capstonebe.inquiry.entity.FAQ;
import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    @Query("select f from Faq f where f.type = :type")
    Page<FAQ> findAllByFaqType(QuestionType type, Pageable pageable);
}
