package com.capstonebe.capstonebe.inquiry.repository;

import com.capstonebe.capstonebe.inquiry.entity.FAQ;
import com.capstonebe.capstonebe.inquiry.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findAll(Pageable pageable);
    Page<FAQ> findAllByQuestionType(QuestionType type, Pageable pageable);
}
