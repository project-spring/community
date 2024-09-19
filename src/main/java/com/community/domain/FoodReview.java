package com.community.domain;

// JPA(Java Persistence API)의 엔티티와 관련된 어노테이션들이 들어있는 패키지입니다.
import jakarta.persistence.*;

// @Getter, @Setter, @ToString과 같은 Lombok 어노테이션을 사용하기 위한 패키지입니다.
// Lombok을 사용하면 반복적인 코드를 줄일 수 있습니다.

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Java에서 Collection 타입을 사용할 수 있도록 해주는 패키지입니다.
// Java에서 컬렉션(데이터 모음)을 다루기 위한 인터페이스, 여러 개의 데이터를 모아서 처리할 때 유용, List, Set 등이 이 인터페이스를 구현
// 여러 데이터 구조(Collection, Set 등)를 제공하며, 데이터 그룹을 다룰 때 유용합니다.
import java.util.Collection;

// LinkedHashSet은 Set 인터페이스의 구현체 중 하나로,
// 삽입된 순서를 유지하면서 중복을 허용하지 않는 컬렉션입니다.
// 데이터를 삽입한 순서를 유지하면서, 중복된 값을 허용하지 않습니다. 해시태그나 댓글 목록 등 중복되지 않는 데이터를 순서대로 저장할 때 유용
import java.util.LinkedHashSet;

// Objects는 객체 비교와 해시코드 계산 등에 유용한 메서드를 제공합니다.
// equals와 hashCode 메서드 구현 시 자주 사용됩니다.
import java.util.Objects;

// Set은 중복된 요소를 허용하지 않는 컬렉션을 나타내는 인터페이스입니다.
// 게시글과 해시태그 간의 관계나 댓글 목록 등을 저장할 때 사용됩니다.
import java.util.Set;

// 이 클래스는 데이터베이스의 테이블과 매핑됩니다.
@Entity

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
    protected Article() {}

    // 게시글 생성 시 반드시 유저 정보와 제목, 내용을 받아야 하는 생성자입니다.
    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    // 게시글을 생성할 때 사용하는 정적 팩토리 메서드입니다.
    // new 키워드를 사용하지 않고 객체를 생성할 수 있습니다.
    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    // 두 Article 객체를 비교할 때 사용하는 메서드입니다.
    // 동일한 id 값을 가진 객체는 같다고 판단합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체면 true
        if (!(o instanceof Article that)) return false; // Article이 아니면 false
        return this.getId() != null && this.getId().equals(that.getId()); // id가 같으면 true
    }

}
