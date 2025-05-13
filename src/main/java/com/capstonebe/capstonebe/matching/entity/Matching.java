package com.capstonebe.capstonebe.matching.entity;

import com.capstonebe.capstonebe.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lost_item_id")
    private Item lostItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "found_item_id")
    private Item foundItem;

    @Enumerated(EnumType.STRING)
    private MatchingState state;
}
