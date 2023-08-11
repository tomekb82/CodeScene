package com.mlbn.appoint.shared;

import com.mlbn.appoint.domain.appointment.*;
import com.mlbn.appoint.shared.vo.*;
import com.mlbn.appoint.domain.facility.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.*;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class Fixtures {

    Instant fixedInstant = Instant.parse("2023-07-20T07:34:56Z");

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
        return aProduct(aConfigurationSlot());
    }

    public Product aProduct(ConfigurationSlot configurationSlot) {
        return new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(configurationSlot),
                Set.of());
    }

    public Product aProduct(Set<ClosedSlot> closedSlots) {
        return new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(aConfigurationSlot()),
                closedSlots);
    }

    public Facility aFacility() {
        return new Facility(aFacilityId(),
                "name",
                aFacilityAddress(),
                new PhoneNumber("12345678"),
                Set.of(aProduct()));
    }

    public BookingSlot aBookingSlot() {
        return aBookingSlot(aProductId());
    }

    public BookingSlot aBookingSlot(ProductId productId) {
        return new BookingSlot(productId, aStartDate(), Duration.ofHours(1));
    }

    public BookingSlot aBookingSlot2() {
        return aBookingSlot2(aProductId());
    }

    public BookingSlot aBookingSlot2(ProductId productId) {
        return new BookingSlot(productId,aStartDate().plusHours(3), Duration.ofHours(1));
    }

    public BookingSlot aBookingSlotInThePastWithNegativeDuration() {
        return new BookingSlot(aProductId(), aStartDate().minusHours(5), Duration.ZERO.minusDays(2));
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
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        BookingSlot slot = aBookingSlot();
        return BookingCommand.of(clientId, facilityId, employeeId, slot, comment, clock).get();
    }

    public BookingCommand aBookingCommand(ProductId productId){
        ClientId clientId = aClientId();
        FacilityId facilityId = aFacilityId();
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        BookingSlot slot = aBookingSlot(productId);
        return BookingCommand.of(clientId, facilityId, employeeId, slot, comment, clock).get();
    }

    public BookingCommand aBookingCommand(BookingSlot slot){
        ClientId clientId = aClientId();
        FacilityId facilityId = aFacilityId();
        EmployeeId employeeId = anEmployeeId();
        Comment comment = new Comment("test");
        return BookingCommand.of(clientId, facilityId, employeeId, slot, comment, clock).get();
    }

    public ConfigurationSlot aConfigurationSlot() {
        return new ConfigurationSlot(aStartDate().getDayOfWeek(), Set.of(aSlot(), aSlot2()));
    }

    public ClosedSlot aClosedSlot() {
        return new ClosedSlot(aStartDate().toLocalDate(), Set.of(aSlot()));
    }

    public TimeSlot aSlot() {
        return aSlot(0, 30);
    }

    public TimeSlot aSlot2() {
        return aSlot(3, 120);
    }

    public TimeSlot aSlot(int offsetInHours, int durationInMinutes) {
        return TimeSlot.of(aStartDate().plusHours(offsetInHours).toLocalTime(), Duration.ZERO.plusMinutes(durationInMinutes));
    }

    public FacilityAddress aFacilityAddress() {
        return new FacilityAddress("streetName", "streetNumber",
                "apartmentNumber", "city", "zipCode",
                "country", new GeoPoint(1,2));
    }
}
