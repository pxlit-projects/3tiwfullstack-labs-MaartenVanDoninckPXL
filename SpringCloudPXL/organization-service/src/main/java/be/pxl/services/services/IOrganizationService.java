package be.pxl.services.services;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;

public interface IOrganizationService {
    void createOrganization(OrganizationRequest organization);
    OrganizationResponse getOrganizationById(Long id);
    OrganizationResponse getOrganizationByIdWithDepartments(Long id);
    OrganizationResponse getOrganizationByIdWithEmployees(Long id);
    OrganizationResponse getOrganizationByIdWithDepartmentsAndEmployees(Long id);
}
