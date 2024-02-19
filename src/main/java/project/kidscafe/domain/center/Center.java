package project.kidscafe.domain.center;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kidscafe.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private Long id;
    @Column(length = 16)
    private String centerName;
    @Column(length = 64)
    private String centerAddress;

    @Builder
    private Center(String centerName, String centerAddress) {
        this.centerName = centerName;
        this.centerAddress = centerAddress;
    }
    public static Center create(String centerName, String centerAddress) {
        return Center.builder()
                .centerName(centerName)
                .centerAddress(centerAddress)
                .build();
    }

}
