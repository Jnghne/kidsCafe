package project.kidscafe.domain.reservation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = -458103697L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final project.kidscafe.domain.QBaseEntity _super = new project.kidscafe.domain.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> cancelDt = createDateTime("cancelDt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final project.kidscafe.domain.user.QParent parent;

    public final project.kidscafe.domain.program.QProgramSchedule programSchedule;

    public final ListPath<ReservationChild, QReservationChild> reservationChildren = this.<ReservationChild, QReservationChild>createList("reservationChildren", ReservationChild.class, QReservationChild.class, PathInits.DIRECT2);

    public final EnumPath<ReservationStatus> reservationStatus = createEnum("reservationStatus", ReservationStatus.class);

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new project.kidscafe.domain.user.QParent(forProperty("parent")) : null;
        this.programSchedule = inits.isInitialized("programSchedule") ? new project.kidscafe.domain.program.QProgramSchedule(forProperty("programSchedule"), inits.get("programSchedule")) : null;
    }

}

