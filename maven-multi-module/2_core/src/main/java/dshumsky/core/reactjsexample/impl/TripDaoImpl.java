package dshumsky.core.reactjsexample.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.google.inject.persist.Transactional;
import dshumsky.core.reactjsexample.api.TripDao;
import dshumsky.core.reactjsexample.model.Trip;
import dshumsky.core.reactjsexample.model.Trip.Props;
import dshumsky.core.reactjsexample.model.Trip.Table;
import dshumsky.core.reactjsexample.model.TripFilter;
import dshumsky.core.reactjsexample.model.TripResource;
import dshumsky.core.reactjsexample.model.User;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
@Transactional
class TripDaoImpl extends AbstractDao implements TripDao {

    @Inject
    public TripDaoImpl(Provider<EntityManager> emProvider) {
        super(emProvider);
    }

    @Override
    public Trip findById(long id) {
        Criteria criteria = getSession().createCriteria(Trip.class);
        criteria.add(Restrictions.eq(Trip.Props.id, id));
        return (Trip) criteria.uniqueResult();
    }

    @Override
    public long createOrUpdate(Trip trip) {
        long tripId;
        if (trip.getId() == null) {
            tripId = (long) getSession().save(trip);
        } else {
            getSession().update(trip);
            tripId = trip.getId();
        }
        return tripId;
    }

    @Override
    public void delete(long id) {
        String hql = "delete " + Trip.class.getName() + " where " + Props.id + " = :id";
        Query q = getSession().createQuery(hql).setParameter("id", id);
        q.executeUpdate();
    }

    private static final String Q_GET_TRIPS = "select <columns>\n" +
            "from T04_TRIP\n" +
            "inner join T01_USER on C04_USER_ID=C01_USER_ID\n" +
            "where <conditions> \n" +
            "order by C04_TRIP_ID desc";

    @Override
    public List<TripResource> getTrips(TripFilter tripFilter) {
        //@formatter:off
        QueryColumnCollector columns = new QueryColumnCollector()
                .add(Table.C04_TRIP_ID,       TripResource.Props.trip_id,          LongType.INSTANCE)
                .add(Table.C04_USER_ID,       TripResource.Props.trip_userId,      LongType.INSTANCE)
                .add(Table.C04_DESTINATION,   TripResource.Props.trip_destination, StringType.INSTANCE)
                .add(Table.C04_START_DATE,    TripResource.Props.trip_startDate,   DateType.INSTANCE)
                .add(Table.C04_END_DATE,      TripResource.Props.trip_endDate,     DateType.INSTANCE)
                .add(Table.C04_COMMENT,       TripResource.Props.trip_comment,     StringType.INSTANCE)
                .add(User.Table.C01_USER_ID,  TripResource.Props.user_id,          LongType.INSTANCE)
                .add(User.Table.C01_USERNAME, TripResource.Props.user_username,    StringType.INSTANCE);
        //@formatter:on
        QueryConditionCollector conditions = new QueryConditionCollector()
                .add(tripFilter.getUserId() != null, User.Table.C01_USER_ID + "=:userId", q -> {
                    q.setParameter("userId", tripFilter.getUserId());
                    return null;
                })
                .add(tripFilter.getDestination() != null, Table.C04_DESTINATION + " like :destination", q -> {
                    q.setParameter("destination", "%"+tripFilter.getDestination()+"%");
                    return null;
                })
                .add(tripFilter.getStartDateFrom() != null, Table.C04_START_DATE + ">=trunc(:startDateFrom)", q -> {
                    q.setParameter("startDateFrom", tripFilter.getStartDateFrom());
                    return null;
                })
                .add(tripFilter.getEndDateUntil() != null, Table.C04_END_DATE + "<=trunc(:endDateUntil)", q -> {
                    q.setParameter("endDateUntil", tripFilter.getEndDateUntil());
                    return null;
                });
        SQLQuery query = getSession().createSQLQuery(
                Q_GET_TRIPS.replace("<columns>", columns.getColumnsWithAliases())
                        .replace("<conditions>", conditions.sqlUsingAnd()));

        conditions.setParameters(query);
        columns.addScalars(query);
        query.setResultTransformer(new NestedAliasToBeanResultTransformer<>(TripResource.class, columns.getRenamedAliasMap()));

        // noinspection unchecked
        return query.list();
    }
}
