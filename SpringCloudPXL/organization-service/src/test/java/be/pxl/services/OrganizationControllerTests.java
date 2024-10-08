package be.pxl.services;

import be.pxl.services.domain.Organization;
import be.pxl.services.repository.OrganizationRepository;
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

@SpringBootTest(classes = OrganizationServiceApp.class)
@Testcontainers
@AutoConfigureMockMvc
public class OrganizationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrganizationRepository organizationRepository;

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
    public void testCreateOrganization() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Elfde-Liniestraat 24")
                .build();
        String organizationJson = objectMapper.writeValueAsString(organization);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(organizationJson))
                .andExpect(status().isCreated());

        assertEquals(1, organizationRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testGetOrganizationById() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Elfde-Liniestraat 24")
                .build();
        organizationRepository.save(organization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetOrganizationByIdWithDepartments() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Elfde-Liniestraat 24")
                .build();
        organizationRepository.save(organization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/1/with-departments"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetOrganizationByIdWithEmployees() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Elfde-Liniestraat 24")
                .build();
        organizationRepository.save(organization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/1/with-employees"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testGetOrganizationByIdWithDepartmentsAndEmployees() throws Exception {
        Organization organization = Organization.builder()
                .name("PXL")
                .address("Elfde-Liniestraat 24")
                .build();
        organizationRepository.save(organization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/1/with-departments-and-employees"))
                .andExpect(status().isOk());
    }
}
