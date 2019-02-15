package dshumsky.web.reactjsexample.rest;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import dshumsky.web.reactjsexample.security.PermissionChecker;

import dshumsky.core.reactjsexample.api.TripDao;
import dshumsky.core.reactjsexample.model.Trip;
import dshumsky.core.reactjsexample.model.TripFilter;
import dshumsky.core.reactjsexample.model.TripResource;
import dshumsky.core.reactjsexample.model.TripResource.Props;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import static dshumsky.core.reactjsexample.model.PermissionType.CRUD_ALL_TRIPS;
import static dshumsky.core.reactjsexample.model.PermissionType.CRUD_OWN_TRIPS;

@RestController
public class TripRestController {

    private final TripDao tripDao;

    public TripRestController(@Autowired TripDao tripDao) {
        this.tripDao = tripDao;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "trips")
    @JsonView(TripResource.ViewTrips.class)
    public List<TripResource> getTrips(
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "startDateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateFrom,
            @RequestParam(value = "endDateUntil", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateUntil) {
        getPermissionChecker().checkOneOf(CRUD_ALL_TRIPS, CRUD_OWN_TRIPS);
        TripFilter tripFilter = new TripFilter();
        tripFilter.setDestination(destination);
        tripFilter.setStartDateFrom(startDateFrom);
        tripFilter.setEndDateUntil(endDateUntil);
        if (!getPermissionChecker().has(CRUD_ALL_TRIPS)) {
            tripFilter.setUserId(getPermissionChecker().getUserId());
        }
        return tripDao.getTrips(tripFilter);
    }

    @RequestMapping(method = RequestMethod.GET, value = "trips/pdf")
    public ResponseEntity<byte[]> getTripsPdfP(@RequestParam(value = "destination", required = false) String destination,
                                               @RequestParam(value = "startDateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDateFrom,
                                               @RequestParam(value = "endDateUntil", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDateUntil) throws Exception {

        List<TripResource> trips = getTrips(destination, startDateFrom, endDateUntil);

        JasperReportBuilder reportBuilder = DynamicReports.report()
                .setTemplate(Templates.reportTemplate)
                .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                .columns(DynamicReports.col.column("Destination", Props.trip_destination, DynamicReports.type.stringType()).setWidth(60))
                .columns(DynamicReports.col.column("Start Date", Props.trip_startDate, DynamicReports.type.dateType()).setWidth(30))
                .columns(DynamicReports.col.column("End Date", Props.trip_endDate, DynamicReports.type.dateType()).setWidth(30))
                .columns(DynamicReports.col.column("Comment", Props.trip_comment, DynamicReports.type.stringType()))
                .title(DynamicReports.cmp.text("Trips"))
                .setDataSource(new JRBeanCollectionDataSource(trips));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        reportBuilder.toPdf(output);

        return pdf("trips.pdf", output.toByteArray());
    }

    private ResponseEntity<byte[]> pdf(String filename, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "trip/{id}", method = RequestMethod.GET)
    public Trip getTrip(@PathVariable("id") long id) {
        Trip trip = getTripWithPermissionCheck(id);
        return trip;
    }

    private Trip getTripWithPermissionCheck(@PathVariable("id") long id) {
        getPermissionChecker().checkOneOf(CRUD_ALL_TRIPS, CRUD_OWN_TRIPS);
        Trip trip = tripDao.findById(id);
        if (!trip.getUserId().equals(getPermissionChecker().getUserId())) {
            getPermissionChecker().checkOneOf(CRUD_ALL_TRIPS);
        }
        return trip;
    }

    @RequestMapping(value = "trip/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteTrip(@PathVariable("id") long id) {
        Trip trip = getTripWithPermissionCheck(id);
        tripDao.delete(trip.getId());
        return ResponseEntity.ok(id);
    }

    @RequestMapping(value = "trip/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public long updateTrip(@PathVariable("id") long id, @RequestBody Trip trip) {
        getPermissionChecker().checkOneOf(CRUD_ALL_TRIPS, CRUD_OWN_TRIPS);
        Trip existingTrip = tripDao.findById(id);
        if (!getPermissionChecker().has(CRUD_ALL_TRIPS)) {
            if (!existingTrip.getUserId().equals(getPermissionChecker().getUserId())) {
                throw new AccessDeniedException("CRUD own trips only");
            }
        }
        trip.setId(id);
        trip.setUserId(existingTrip.getUserId());
        tripDao.createOrUpdate(trip);
        return id;
    }

    @RequestMapping(value = "trips", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public long createTrip(@RequestBody Trip trip) {
        trip.setId(null);
        trip.setUserId(getPermissionChecker().getUserId());
        return tripDao.createOrUpdate(trip);
    }

    private PermissionChecker getPermissionChecker() {
        return new PermissionChecker(SecurityContextHolder.getContext().getAuthentication());
    }
}
