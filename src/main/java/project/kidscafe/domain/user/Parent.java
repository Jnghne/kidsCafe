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
public class Parent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long id;
    @Column(length = 4)
    private String parentName;
    @Column(length = 32)
    private String email;

    @Builder
    private Parent(String parentName, String email) {
        this.parentName=parentName;
        this.email = email;
    }

    public static Parent create(String parentName, String email) {
        return Parent.builder()
                .parentName(parentName)
                .email(email)
                .build();
    }
}

