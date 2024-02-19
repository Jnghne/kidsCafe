package project.kidscafe.domain.program;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import project.kidscafe.domain.center.Center;
import project.kidscafe.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Program extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long id;

    @Column(length = 16)
    private String programName;

    @ColumnDefault("20")
    private int maxReservationCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    @Builder
    private Program(String programName, Center center, Integer maxReservationCnt){
        this.programName = programName;
        this.center = center;
        this.maxReservationCnt = maxReservationCnt;
    }

    public static Program create(String programName, Center center, int maxReservationCnt) {
        return Program.builder()
                .programName(programName)
                .center(center)
                .maxReservationCnt(maxReservationCnt)
                .build();
    }
}
