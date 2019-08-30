package dshumsky.core.reactjsexample.api;

import java.util.List;

import dshumsky.core.reactjsexample.model.Trip;
import dshumsky.core.reactjsexample.model.TripFilter;
import dshumsky.core.reactjsexample.model.TripResource;

public interface TripDao {
    List<TripResource> getTrips(TripFilter tripFilter);

    Trip findById(long id);

    long createOrUpdate(Trip trip);

    void delete(long id);
}
