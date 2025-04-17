package com.capstonebe.capstonebe.keword.entity;

import com.capstonebe.capstonebe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "keyword"})}
)
public class Keyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Keyword() {}

    @Builder
    public Keyword(String keyword, User user) {
        this.keyword = keyword;
        this.user = user;
    }
}
