package com.capstonebe.capstonebe.post.repository;

import com.capstonebe.capstonebe.itemplace.entity.QItemPlace;
import com.capstonebe.capstonebe.post.entity.Post;
import com.capstonebe.capstonebe.post.entity.QPost;
import com.capstonebe.capstonebe.item.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPosts(Long categoryId, Long placeId, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        QPost post = QPost.post;
        QItem item = QItem.item;
        QItemPlace itemPlace = QItemPlace.itemPlace;

        BooleanBuilder builder = new BooleanBuilder();

        if (categoryId != null) {
            builder.and(item.category.id.eq(categoryId));
        }

        if (placeId != null) {
            List<Long> itemIds = queryFactory
                    .select(itemPlace.item.id)
                    .from(itemPlace)
                    .where(itemPlace.place.id.eq(placeId))
                    .fetch();

            if (itemIds.isEmpty()) {
                return PageableExecutionUtils.getPage(List.of(), pageable, () -> 0);
            }
            builder.and(item.id.in(itemIds));
        }

        if (keyword != null && !keyword.isBlank()) {
            builder.and(post.title.containsIgnoreCase(keyword)
                    .or(post.content.containsIgnoreCase(keyword)));
        }

        if (startDate != null) {
            builder.and(post.createdAt.goe(startDate.atStartOfDay()));
        }

        if (endDate != null) {
            builder.and(post.createdAt.loe(endDate.atTime(LocalTime.MAX)));
        }

        List<Post> results = queryFactory
                .selectFrom(post)
                .join(post.item, item).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .join(post.item, item)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }
}