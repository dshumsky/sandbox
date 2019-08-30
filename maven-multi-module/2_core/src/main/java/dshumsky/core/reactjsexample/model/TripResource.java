package dshumsky.core.reactjsexample.model;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class TripResource {

    //@formatter:off
    public interface Props {
        String trip = "trip",
               user = "user",
               user_id          = user + "." + User.Props.id,
               user_username    = user + "." + User.Props.username,
               user_password    = user + "." + User.Props.password,
               user_firstname   = user + "." + User.Props.firstname,
               user_lastname    = user + "." + User.Props.lastname,
               user_email       = user + "." + User.Props.email,
               user_enabled     = user + "." + User.Props.enabled,
               trip_id          = trip + "." + Trip.Props.id,
               trip_userId      = trip + "." + Trip.Props.userId,
               trip_destination = trip + "." + Trip.Props.destination,
               trip_startDate   = trip + "." + Trip.Props.startDate,
               trip_endDate     = trip + "." + Trip.Props.endDate,
               trip_comment     = trip + "." + Trip.Props.comment;
    }
    //@formatter:on

    public interface ViewTrips {}


    private Trip trip;
    private User user;

    public TripResource() {
        trip = new Trip();
        user = new User();
    }

    @JsonView(ViewTrips.class)
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @JsonView(ViewTrips.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
