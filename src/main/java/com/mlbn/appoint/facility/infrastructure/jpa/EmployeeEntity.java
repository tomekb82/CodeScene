package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.facility.domain.Employee;
import com.mlbn.appoint.facility.domain.EmployeeId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "employees")
class EmployeeEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    public EmployeeEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee toEmployee() {
        return new Employee(new EmployeeId(id), firstName, lastName);
    }

    public static EmployeeEntity from(Employee employee) {
        return new EmployeeEntity(employee.firstName(), employee.lastName());
    }

    public static Set<EmployeeEntity> from(Set<Employee> employees) {
        return employees.stream()
                .map(EmployeeEntity::from)
                .collect(Collectors.toSet());
    }
}
