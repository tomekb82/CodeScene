package com.mlbn.appoint.domain.appointment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
class TestConfig {

    private final ApplicationEventPublisher publisher = new MockApplicationEventPublisher();
    private final Appointments appointments = new InMemoryAppointments();
    private final Products products = new InMemoryProducts();

    public SlotService slotService(){
        return new SlotService(appointments, products, fixedClock());
    }

    public BookingService bookingService(){
        return new BookingService(appointments, slotService(), publisher(), fixedClock());
    }

    public CancelingService cancelingService(){
        return new CancelingService(appointments, publisher(), fixedClock());
    }

    public ApplicationEventPublisher publisher() {
        return publisher;
    }

    public Appointments appointments() {
        return appointments;
    }

    public Products products() {
        return products;
    }

    @Bean
    AppointEventListener appointEventListener() {
        return new AppointEventListener();
    }

    @Bean
    @Primary
    public Clock fixedClock() {
        Instant fixedInstant = Instant.parse("2023-07-20T07:34:56Z");
        return Clock.fixed(fixedInstant, ZoneId.of("UTC"));
    }
}
