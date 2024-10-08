package be.pxl.services.controller;

import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrganization(@RequestBody OrganizationRequest organization) {
        organizationService.createOrganization(organization);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(organizationService.getOrganizationById(id), HttpStatus.OK);
        } catch (IllegalIdentifierException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity<?> getOrganizationByIdWithDepartments(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(organizationService.getOrganizationByIdWithDepartments(id), HttpStatus.OK);
        } catch (IllegalIdentifierException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity<?> getOrganizationByIdWithEmployees(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(organizationService.getOrganizationByIdWithEmployees(id), HttpStatus.OK);
        } catch (IllegalIdentifierException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity<?> getOrganizationByIdWithDepartmentsAndEmployees(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(organizationService.getOrganizationByIdWithDepartmentsAndEmployees(id), HttpStatus.OK);
        } catch (IllegalIdentifierException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
