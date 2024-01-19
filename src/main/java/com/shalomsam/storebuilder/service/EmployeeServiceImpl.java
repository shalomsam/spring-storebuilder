package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.user.Employee;
import com.shalomsam.storebuilder.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements DomainService<Employee> {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Employee> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Employee> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Employee> create(Employee entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Employee> updateById(String id, Employee entity) {
        return repository.findById(id)
            .map(employee -> {

                if (entity.getFirstName() != null) employee.setFirstName(entity.getFirstName());
                if (entity.getLastName() != null) employee.setLastName(entity.getLastName());
                if (entity.getEmail() != null) employee.setEmail(entity.getEmail());
                if (entity.getAuthToken() != null) employee.setAuthToken(entity.getAuthToken());
                if (entity.getPhoneNumber() != null) employee.setPhoneNumber(entity.getPhoneNumber());
                if (entity.getOrganization() != null) employee.setOrganization(entity.getOrganization());
                if (entity.getEmployeeAccess() != null) employee.setEmployeeAccess(entity.getEmployeeAccess());
                if (entity.getEmployeeAddress() != null) employee.setEmployeeAddress(entity.getEmployeeAddress());

                return employee;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Employee> updateMany(List<Employee> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return repository.deleteById(id).thenReturn(1);
    }

    @Override
    public Mono<Integer> deleteManyById(List<String> ids) {
        return repository.deleteAllByIdIn(ids);
    }

    @Override
    public Mono<Long> getCount() {
        return repository.count();
    }
}
