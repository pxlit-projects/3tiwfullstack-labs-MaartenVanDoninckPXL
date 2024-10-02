package be.pxl.services.services;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;

import java.util.List;

public interface IDepartmentService {
    void addDepartment(DepartmentRequest departmentRequest);
    DepartmentResponse getDepartmentById(Long id);
    List<DepartmentResponse> getAllDepartments();
    List<DepartmentResponse> getDepartmentsByOrganizationId(Long organizationId);
    List<DepartmentResponse> getDepartmentsByOrganizationIdWithEmployees(Long organizationId);
}
