package project.kidscafe.domain.program;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProgramSchedule is a Querydsl query type for ProgramSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProgramSchedule extends EntityPathBase<ProgramSchedule> {

    private static final long serialVersionUID = 321047958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProgramSchedule programSchedule = new QProgramSchedule("programSchedule");

    public final project.kidscafe.domain.QBaseEntity _super = new project.kidscafe.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final QProgram program;

    public final NumberPath<Integer> reservationCnt = createNumber("reservationCnt", Integer.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public QProgramSchedule(String variable) {
        this(ProgramSchedule.class, forVariable(variable), INITS);
    }

    public QProgramSchedule(Path<? extends ProgramSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProgramSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProgramSchedule(PathMetadata metadata, PathInits inits) {
        this(ProgramSchedule.class, metadata, inits);
    }

    public QProgramSchedule(Class<? extends ProgramSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.program = inits.isInitialized("program") ? new QProgram(forProperty("program"), inits.get("program")) : null;
    }

}

