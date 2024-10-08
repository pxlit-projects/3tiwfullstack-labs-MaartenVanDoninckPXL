package be.pxl.services.controller;

import be.pxl.services.DepartmentServiceApp;
import be.pxl.services.domain.Department;
import be.pxl.services.repository.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DepartmentServiceApp.class)
@Testcontainers
@AutoConfigureMockMvc
public class DepartmentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Container
    private static final MySQLContainer<?> sqlContainer = new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @Test
    @Transactional
    public void testCreateDepartment() throws Exception {
        Department department = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        String departmentJson = objectMapper.writeValueAsString(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(departmentJson))
                .andExpect(status().isCreated());

        assertEquals(1, departmentRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testGetDepartmentById() throws Exception {
        Department department = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/" + department.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetAllDepartments() throws Exception {
        Department department1 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department1);

        Department department2 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department"))
                .andExpect(status().isOk());

        assertEquals(2, departmentRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testGetDepartmentsByOrganizationId() throws Exception {
        Department department1 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department1);

        Department department2 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/organization/1"))
                .andExpect(status().isOk());

        assertEquals(2, departmentRepository.findByOrganizationId(1L).size());
    }

    @Test
    @Transactional
    public void testGetDepartmentsByOrganizationIdWithEmployees() throws Exception {
        Department department1 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department1);

        Department department2 = Department.builder()
                .name("IT")
                .organizationId(1L)
                .position("Student")
                .build();
        departmentRepository.save(department2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/organization/1/with-employees"))
                .andExpect(status().isOk());

        assertEquals(2, departmentRepository.findByOrganizationId(1L).size());
    }
}
