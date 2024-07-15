package com.ripplereach.ripplereach.specifications;

import com.ripplereach.ripplereach.models.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
    public static Specification<Post> containsTextInTitleOrContentOrCommunity(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("content")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("community").get("name")), "%" + search.toLowerCase() + "%")
        );
    }
}
