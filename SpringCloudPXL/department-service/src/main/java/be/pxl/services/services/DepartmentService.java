package be.pxl.services.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.domain.Department;
import be.pxl.services.domain.NotificationRequest;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository departmentRepository;
    private final NotificationClient notificationClient;

    @Override
    public void addDepartment(DepartmentRequest departmentRequest) {
        Department department = Department.builder()
                .name(departmentRequest.getName())
                .build();
        departmentRepository.save(department);

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Employee " + department.getName() + " has been created")
                .sender("Maarten")
                .build();
        notificationClient.sendNotification(notificationRequest);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No department found with id " + id));

        return mapToDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDepartmentResponse).toList();
    }

    @Override
    public List<DepartmentResponse> getDepartmentsByOrganizationId(Long organizationId) {
        List<Department> departments = departmentRepository.findByOrganizationId(organizationId);

        if (departments.isEmpty()) {
            throw new IllegalArgumentException("No departments found for organization with id " + organizationId);
        }

        return departments.stream()
                .map(this::mapToDepartmentResponse).toList();
    }

    @Override
    public List<DepartmentResponse> getDepartmentsByOrganizationIdWithEmployees(Long organizationId) {
        List<Department> departments = departmentRepository.findByOrganizationId(organizationId);

        if (departments.isEmpty()) {
            throw new IllegalArgumentException("No departments found for organization with id " + organizationId);
        }

        return departments.stream()
                .map(this::mapToDepartmentResponseWithEmployees).toList();
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .position(department.getPosition())
                .build();
    }

    private DepartmentResponse mapToDepartmentResponseWithEmployees(Department department) {
        return DepartmentResponse.builder()
                .organizationId(department.getOrganizationId())
                .name(department.getName())
                .employees(department.getEmployees())
                .position(department.getPosition())
                .build();
    }
}
