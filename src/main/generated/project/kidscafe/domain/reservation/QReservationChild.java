package project.kidscafe.domain.reservation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationChild is a Querydsl query type for ReservationChild
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationChild extends EntityPathBase<ReservationChild> {

    private static final long serialVersionUID = 875007469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationChild reservationChild = new QReservationChild("reservationChild");

    public final project.kidscafe.domain.QBaseEntity _super = new project.kidscafe.domain.QBaseEntity(this);

    public final project.kidscafe.domain.user.QChild child;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final QReservation reservation;

    public QReservationChild(String variable) {
        this(ReservationChild.class, forVariable(variable), INITS);
    }

    public QReservationChild(Path<? extends ReservationChild> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationChild(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationChild(PathMetadata metadata, PathInits inits) {
        this(ReservationChild.class, metadata, inits);
    }

    public QReservationChild(Class<? extends ReservationChild> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.child = inits.isInitialized("child") ? new project.kidscafe.domain.user.QChild(forProperty("child"), inits.get("child")) : null;
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

