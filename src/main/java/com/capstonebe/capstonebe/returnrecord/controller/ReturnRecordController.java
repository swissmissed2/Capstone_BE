package com.capstonebe.capstonebe.returnrecord.controller;

import com.capstonebe.capstonebe.returnrecord.service.ReturnRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/returns")
@RequiredArgsConstructor
public class ReturnRecordController {

    private final ReturnRecordService returnRecordService;

    /**
     * 단일 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseEntity.ok(returnRecordService.findById(id));
    }

    /**
     * 목록 조회
     * 관리자만 가능
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllReturnRecords(Pageable pageable) {

        return ResponseEntity.ok(returnRecordService.getAllReturnRecords(pageable));
    }

}
