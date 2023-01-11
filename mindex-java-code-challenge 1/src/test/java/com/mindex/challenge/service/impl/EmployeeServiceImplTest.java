package com.mindex.challenge.service.impl;

import com.mindex.challenge.controller.EmployeeController;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportsUrl;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeReportsUrl="http://localhost:" + port + "/employee/{id}/numberOfReports";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }
    @Test
    public void testDirectReports() {
      Employee testEmployee1 = new Employee();
      testEmployee1.setFirstName("Naresh");
      testEmployee1.setLastName("Chainani");
      testEmployee1.setDepartment("Engineering");
      testEmployee1.setPosition("Director");

      Employee testEmployee2 = new Employee();
      testEmployee2.setFirstName("Stefan");
      testEmployee2.setLastName("Gromoll");
      testEmployee2.setDepartment("Engineering");
      testEmployee2.setPosition("Development Manager");

      Employee testEmployee3 = new Employee();
      testEmployee3.setFirstName("Nikos");
      testEmployee3.setLastName("Armenatzoglou");
      testEmployee3.setDepartment("Engineering");
      testEmployee3.setPosition("Development Manager");

      Employee testEmployee4 = new Employee();
      testEmployee4.setFirstName("Dominique");
      testEmployee4.setLastName("Sandraz");
      testEmployee4.setDepartment("Engineering");
      testEmployee4.setPosition("Developer");

      // DirectReports check
      Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
      Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();

      ArrayList<Employee> employeeDirectReports1 = new ArrayList<>();
      employeeDirectReports1.add(createdEmployee2);
      employeeDirectReports1.add(createdEmployee3);

      testEmployee1.setDirectReports(employeeDirectReports1);

      Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
      Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();


      ReportingStructure readEmployee1Reports = restTemplate.getForEntity(employeeReportsUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();

      assertEquals(2, readEmployee1Reports.getNumberOfReports());

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
