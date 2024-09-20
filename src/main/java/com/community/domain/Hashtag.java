package com.community.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Getter
@ToString(callSuper = true)
public class Hashtag extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false) private String hashtagName; // 해시태그 이름

    @ManyToMany(mappedBy = "hashtags")
    @ToString.Exclude
    private Set<Article> articles = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "hashtags")
    @ToString.Exclude
    private Set<TravelReview> travelReviews = new LinkedHashSet<>();

    protected Hashtag() {}

    private Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public static Hashtag of(String hashtagName) {
        return new Hashtag(hashtagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
