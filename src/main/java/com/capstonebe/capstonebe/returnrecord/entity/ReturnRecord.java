package com.capstonebe.capstonebe.returnrecord.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ReturnRecord extends BaseEntity {

    @Id @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime returnDate;

    @Builder
    public ReturnRecord(long id, Item item, User user, LocalDateTime returnDate) {
        this.id = id;
        this.item = item;
        this.user = user;
        this.returnDate = returnDate;
    }

}
