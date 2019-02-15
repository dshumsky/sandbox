package dshumsky.core.reactjsexample.model;

import java.util.Date;

/**
 * @author dshumski (Dmitry.Shumski@mgm-tp.com)
 */
public class TripFilter {
    private Long userId;
    private String destination;
    private Date startDateFrom;
    private Date endDateUntil;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(Date startDate) {
        this.startDateFrom = startDate;
    }

    public Date getEndDateUntil() {
        return endDateUntil;
    }

    public void setEndDateUntil(Date endDate) {
        this.endDateUntil = endDate;
    }
}
