package project.kidscafe.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kidscafe.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Child extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;
    @Column(length = 4)
    private String childName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Builder
    private Child(String childName, Parent parent) {
        this.childName = childName;
        this.parent = parent;
    }

    public static Child create(String childName, Parent parent) {
        return Child.builder()
                .childName(childName)
                .parent(parent)
                .build();
    }

}
