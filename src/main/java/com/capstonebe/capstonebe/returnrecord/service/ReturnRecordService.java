package com.capstonebe.capstonebe.returnrecord.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.returnrecord.dto.response.ReturnRecordResponse;
import com.capstonebe.capstonebe.returnrecord.entity.ReturnRecord;
import com.capstonebe.capstonebe.returnrecord.repository.ReturnRecordRepository;
import com.capstonebe.capstonebe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReturnRecordService {

    private final ReturnRecordRepository returnRecordRepository;

    @Transactional
    public void registerReturnRecord(Item item, User user, LocalDateTime dateTime) {

        ReturnRecord returnRecord = ReturnRecord.builder()
                .item(item)
                .user(user)
                .returnDate(dateTime)
                .build();

        returnRecordRepository.save(returnRecord);
    }

    public Page<ReturnRecordResponse> getAllReturnRecords(Pageable pageable) {

        return returnRecordRepository.findAll(pageable)
                .map(ReturnRecordResponse::from);
    }

    public ReturnRecordResponse findById(Long id) {

        ReturnRecord returnRecord = returnRecordRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.RETURN_RECORD_NOT_FOUND));

        return ReturnRecordResponse.from(returnRecord);
    }
}
