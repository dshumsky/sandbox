package dshumsky.core.reactjsexample.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;
import dshumsky.core.reactjsexample.model.TripResource.ViewTrips;

@Entity
@Table(name = Trip.Table.NAME)
public class Trip {

    private static final String SEQUENCE_GENERATOR = "SEQ_T04_TRIP";

    //@formatter:off
    public interface Table {
        String NAME             = "T04_TRIP",
               C04_TRIP_ID      = "C04_TRIP_ID", 
               C04_USER_ID      = "C04_USER_ID",
               C04_DESTINATION  = "C04_DESTINATION",
               C04_START_DATE   = "C04_START_DATE",
               C04_END_DATE     = "C04_END_DATE",
               C04_COMMENT      = "C04_COMMENT";
    }
    public interface Props {
        String id          = "id",
               userId      = "userId",
               destination = "destination",
               startDate   = "startDate",
               endDate     = "endDate",
               comment     = "comment";    
    }
    //@formatter:on

    @Column(name = Table.C04_TRIP_ID)
    @Id
    @SequenceGenerator(name = SEQUENCE_GENERATOR, sequenceName = "SEQ_T04_TRIP", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    private Long id;

    @Column(name = Table.C04_USER_ID, unique = true)
    private Long userId;

    @Column(name = Table.C04_DESTINATION)
    private String destination;

    @Column(name = Table.C04_START_DATE)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = Table.C04_END_DATE)
    private Date endDate;

    @Column(name = Table.C04_COMMENT)
    private String comment;

    @JsonView(ViewTrips.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(ViewTrips.class)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonView(ViewTrips.class)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonView(ViewTrips.class)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonView(ViewTrips.class)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @JsonView(ViewTrips.class)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}