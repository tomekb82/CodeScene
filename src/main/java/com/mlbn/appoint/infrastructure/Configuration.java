package com.mlbn.appoint.infrastructure;

import com.mlbn.appoint.shared.vo.TimeSlot;
import com.mlbn.appoint.domain.appointment.BookingService;
import com.mlbn.appoint.domain.appointment.CancelingService;
import com.mlbn.appoint.domain.appointment.SlotService;
import com.mlbn.appoint.domain.facility.*;
import com.mlbn.appoint.infrastructure.appointment.jpa.AppointmentJpaRepository;
import com.mlbn.appoint.shared.vo.GeoPoint;
import com.mlbn.appoint.shared.vo.Money;
import com.mlbn.appoint.shared.vo.PhoneNumber;
import com.mlbn.appoint.infrastructure.facility.jpa.FacilityJpaRepository;
import com.mlbn.appoint.infrastructure.facility.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@org.springframework.context.annotation.Configuration
@EnableJpaRepositories
@RequiredArgsConstructor
@Slf4j
class Configuration {

    @Bean
    public Clock systemUtcClock() {
        return Clock.systemUTC();
    }

    @Bean
    BookingService bookingService(AppointmentJpaRepository appointments, SlotService slotService,
                                  ApplicationEventPublisher publisher, Clock clock) {
        return new BookingService(appointments, slotService, publisher, clock);
    }

    @Bean
    CancelingService cancelingService(AppointmentJpaRepository appointments, ApplicationEventPublisher publisher, Clock clock) {
        return new CancelingService(appointments, publisher, clock);
    }

    @Bean
    SlotService slotService(AppointmentJpaRepository appointments, ProductJpaRepository products, Clock clock) {
        return new SlotService(appointments, products, clock);
    }

    @Profile("local")
    @Bean
    CommandLineRunner init(FacilityJpaRepository facilityJpaRepository, ProductJpaRepository productJpaRepository) {
        return args -> {
            FacilityAddress address1 = new FacilityAddress("Jesienna", "26", "1",
                    "Warszawa", "02-755", "Poland", new GeoPoint(1, 2));
            FacilityAddress address2 = new FacilityAddress("Grunwaldzka", "4", "24",
                    "Piaseczno", "05-500", "Poland", new GeoPoint(1, 2));
            TimeSlot timeSlot1 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(15));
            TimeSlot timeSlot2 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(30));
            TimeSlot timeSlot3 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(20));
            TimeSlot timeSlot4 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(40));
            TimeSlot timeSlot5 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(40));
            TimeSlot timeSlot6 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(40));
            TimeSlot timeSlot7 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(40));
            TimeSlot timeSlot8 = TimeSlot.of(generateRandomTime(), Duration.ofMinutes(40));
            ConfigurationSlot slot1 = new ConfigurationSlot(DayOfWeek.MONDAY,
                    Set.of(timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6, timeSlot7, timeSlot8));
            ConfigurationSlot slot2 = new ConfigurationSlot(DayOfWeek.TUESDAY,
                    Set.of(timeSlot1, timeSlot2));
            ConfigurationSlot slot3 = new ConfigurationSlot(DayOfWeek.WEDNESDAY,
                    Set.of(timeSlot3, timeSlot2, timeSlot1));
            ConfigurationSlot slot4 = new ConfigurationSlot(DayOfWeek.THURSDAY,
                    Set.of(timeSlot2, timeSlot3, timeSlot5, timeSlot6, timeSlot7, timeSlot8));
            ConfigurationSlot slot5 = new ConfigurationSlot(DayOfWeek.FRIDAY,
                    Set.of(timeSlot3, timeSlot4, timeSlot2));
            Employee employee1 = new Employee(new EmployeeId(UUID.randomUUID()), "Pawelo", "M.");
            Employee employee2 = new Employee(new EmployeeId(UUID.randomUUID()), "Tomko", "B.");
            Product product1 = new Product(new ProductId(UUID.randomUUID()), ProductStatus.ACTIVE,
                    "Standard haircut", "description 1",
                    ProductCategory.BEARD_SHAPING,
                    Money.of(new BigDecimal("180")).get(),
                    Set.of(employee1, employee2),
                    Set.of(slot1, slot2, slot3, slot4, slot5),
                    Set.of());
            Product product2 = new Product(new ProductId(UUID.randomUUID()), ProductStatus.ACTIVE,
                    "Restyle", "description 2",
                    ProductCategory.BEARD_TRIM,
                    Money.of(new BigDecimal("200")).get(),
                    Set.of(employee1),
                    Set.of(slot1, slot2, slot3, slot4, slot5),
                    Set.of());
            Product product3 = new Product(new ProductId(UUID.randomUUID()), ProductStatus.INACTIVE,
                    "Hair Wash", "description 3",
                    ProductCategory.HAIRCUT_AND_BEARD,
                    Money.of(new BigDecimal("120")).get(),
                    Set.of(employee2),
                    Set.of(slot1, slot2, slot3, slot4, slot5),
                    Set.of());
            Product product4 = new Product(new ProductId(UUID.randomUUID()), ProductStatus.ACTIVE,
                    "Wash & Cut", "description 4",
                    ProductCategory.GEL_MANICURE,
                    Money.of(new BigDecimal("180")).get(),
                    Set.of(), // no employee
                    Set.of(slot1, slot2, slot3, slot4, slot5),
                    Set.of());
            Product product5 = new Product(new ProductId(UUID.randomUUID()), ProductStatus.ACTIVE,
                    "HairCut", "description 5",
                    ProductCategory.BEARD_SHAPING,
                    Money.of(new BigDecimal("160")).get(),
                    Set.of(employee1),
                    Set.of(slot1, slot2, slot3, slot4, slot5),
                    Set.of());
            Set<Product> products1 = Set.of(product1, product2, product3);
            Set<Product> products2 = Set.of(product4, product5);
            Facility facility1 = new Facility(null, "CG barbering", address1, new PhoneNumber("66563333"), products1);
            Facility facility2 = new Facility(null, "Mens form", address2, new PhoneNumber("5542312312"),  products2);
            facilityJpaRepository.save(facility1);
            facilityJpaRepository.save(facility2);

            log.info(facilityJpaRepository.findByProductCategory(ProductCategory.BEARD_SHAPING).stream().findFirst().get().toString());
        };
    }

    private LocalTime generateRandomTime() {
        return LocalTime.MIN.plusMinutes(new Random().nextLong());
    }

}
