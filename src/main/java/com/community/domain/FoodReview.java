package com.community.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Entity

@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Getter // 모든 필드에 대해 getter 메서드를 자동으로 생성해줍니다.
@ToString(callSuper = true) // toString 메서드를 자동으로 생성하되, 상위 클래스의 필드까지 포함합니다.
public class FoodReview extends AuditingFields { // AuditingFields는 생성, 수정 일시 등의 공통 필드를 상속받습니다.

    // 기본 키(PK)로 사용되는 id 필드입니다. 자동으로 증가하는 값으로 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목을 나타내는 필드로, 값이 null일 수 없습니다.
    @Setter @Column(nullable = false) private String title;

    // 게시글 본문을 나타내는 필드로, 최대 길이를 10,000자로 제한하고, 값이 null일 수 없습니다.
    @Setter @Column(nullable = false, length = 10000) private String content;

    // 이 게시글을 작성한 유저 정보를 나타냅니다.
    // userId라는 외래 키(foreign key)로 다른 테이블(UserAccount)과 연결됩니다.
    // ManyToOne은 여러 게시글이 하나의 유저에 속할 수 있음을 의미합니다.
    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount;


    // 기본 생성자입니다. JPA에서는 기본 생성자가 필요하지만, 외부에서는 사용되지 않으므로 protected로 선언합니다.
    protected FoodReview() {}

    // 게시글 생성 시 반드시 유저 정보와 제목, 내용을 받아야 하는 생성자입니다.
    private FoodReview(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    // 게시글을 생성할 때 사용하는 정적 팩토리 메서드입니다.
    // new 키워드를 사용하지 않고 객체를 생성할 수 있습니다.
    public static FoodReview of(UserAccount userAccount, String title, String content) {
        return new FoodReview(userAccount, title, content);
    }

    // 두 Article 객체를 비교할 때 사용하는 메서드입니다.
    // 동일한 id 값을 가진 객체는 같다고 판단합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체면 true
        if (!(o instanceof FoodReview that)) return false; // Article이 아니면 false
        return this.getId() != null && this.getId().equals(that.getId()); // id가 같으면 true
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
