package com.mlbn.appoint.common;

import com.mlbn.appoint.appointment.domain.*;
import com.mlbn.appoint.common.vo.GeoPoint;
import com.mlbn.appoint.common.vo.Money;
import com.mlbn.appoint.common.vo.PhoneNumber;
import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.*;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class Fixtures {

    Instant fixedInstant = Instant.parse("2023-07-20T12:34:56Z");

    public Clock clock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

    private final UUID DEFAULT_UUID = UUID.fromString("a24a6ea4-ce75-4665-a070-57453082c256");

    public AppointmentId anAppointmentId(){
        return new AppointmentId(DEFAULT_UUID);
    }

    public ClientId aClientId(){
        return new ClientId(DEFAULT_UUID);
    }

    public EmployeeId anEmployeeId(){
        return new EmployeeId(DEFAULT_UUID);
    }

    public FacilityId aFacilityId() {
        return new FacilityId(DEFAULT_UUID);
    }

    public ProductId aProductId() {
        return new ProductId(DEFAULT_UUID);
    }

    public Product aProduct() {
        return new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(aConfigurationSlot()));
    }

    public Product aProduct(ConfigurationSlot configurationSlot) {
        return new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(configurationSlot));
    }

    public Facility aFacility() {
        return new Facility(aFacilityId(),
                "name",
                aFacilityAddress(),
                new PhoneNumber("12345678"),
                Set.of(aProduct()));
    }

    public BookingSlot aBookingSlot() {
        return new BookingSlot(aStartDate(), Duration.ofHours(1));
    }

    public BookingSlot aBookingSlot2() {
        return new BookingSlot(aStartDate().plusHours(3), Duration.ofHours(1));
    }

    public BookingSlot aBookingSlotInThePastWithNegativeDuration() {
        return new BookingSlot(aStartDate().minusHours(5), Duration.ZERO.minusDays(2));
    }

    public Comment aComment() {
        return new Comment("test");
    }

    public LocalDateTime aStartDate(){
        return LocalDateTime.now(clock).plusHours(3);
    }

    public BookingCommand aBookingCommand() {
        ClientId clientId = aClientId();
        FacilityId facilityId = aFacilityId();
        ProductId productId = aProductId();
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        BookingSlot slot = aBookingSlot();
        return BookingCommand.of(clientId, facilityId, productId, employeeId, slot, comment, clock).get();
    }

    public BookingCommand aBookingCommand(ProductId productId){
        ClientId clientId = aClientId();
        FacilityId facilityId = aFacilityId();
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        BookingSlot slot = aBookingSlot();
        return BookingCommand.of(clientId, facilityId, productId, employeeId, slot, comment, clock).get();
    }

    public BookingCommand aBookingCommand(ProductId productId, BookingSlot slot){
        ClientId clientId = aClientId();
        FacilityId facilityId = aFacilityId();
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        return BookingCommand.of(clientId, facilityId, productId, employeeId, slot, comment, clock).get();
    }

    public ConfigurationSlot aConfigurationSlot() {
        return new ConfigurationSlot(aStartDate().getDayOfWeek(), Set.of(aSlot(), aSlot2()), Set.of());
    }

    public TimeSlot aSlot() {
        return TimeSlot.of(aStartDate().toLocalTime(), Duration.ZERO);
    }

    public TimeSlot aSlot2() {
        return TimeSlot.of(aStartDate().plusHours(3).toLocalTime(), Duration.ZERO);
    }

    public FacilityAddress aFacilityAddress() {
        return new FacilityAddress("streetName", "streetNumber",
                "apartmentNumber", "city", "zipCode",
                "country", new GeoPoint(1,2));
    }
}
