package be.pxl.services.controller;

import be.pxl.services.EmployeeServiceApp;
import be.pxl.services.domain.Employee;
import be.pxl.services.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EmployeeServiceApp.class)
@Testcontainers
@AutoConfigureMockMvc
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeRepository employeeRepository;

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
    public void testCreateEmployee() throws Exception {
        Employee employee = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .build();
        String employeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated());

        assertEquals(1, employeeRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testGetEmployeeById() throws Exception {
        Employee employee = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .build();
        Employee savedEmployee = employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + savedEmployee.getId()))
                .andExpect(status().isOk());

        assertEquals(savedEmployee, employeeRepository.findById(savedEmployee.getId()).orElseThrow());
    }

    @Test
    @Transactional
    public void testGetEmployees() throws Exception {
        Employee employee1 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .build();
        employeeRepository.save(employee2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"))
                .andExpect(status().isOk());

        assertEquals(2, employeeRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testGetEmployeesByDepartmentId() throws Exception {
        Employee employee1 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .departmentId(1L)
                .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .departmentId(2L)
                .build();
        employeeRepository.save(employee2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/department/1"))
                .andExpect(status().isOk());

        assertEquals(1, employeeRepository.findByDepartmentId(1L).size());
    }

    @Test
    @Transactional
    public void testGetEmployeesByOrganizationId() throws Exception {
        Employee employee1 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .organizationId(1L)
                .build();
        employeeRepository.save(employee1);

        Employee employee2 = Employee.builder()
                .name("Maarten")
                .age(22)
                .position("Student")
                .organizationId(2L)
                .build();
        employeeRepository.save(employee2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/organization/1"))
                .andExpect(status().isOk());

        assertEquals(1, employeeRepository.findByOrganizationId(1L).size());
    }
}