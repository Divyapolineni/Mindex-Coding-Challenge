package com.mindex.challenge.service.impl;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.controller.EmployeeController;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.math.BigDecimal;
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

import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

	private EmployeeRepository employeeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);


    private String compensationGetUrl;
    private String compensationPostUrl;
    private String employeeUrl;


    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
    	compensationGetUrl = "http://localhost:" + port + "/employee/compensation/{employeeId}";
    	compensationPostUrl = "http://localhost:" + port + "/employee/compensation";

    }

    @Test
    public void testCreateAndGetCompensation() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        Compensation employeeCompensation = new Compensation();
        employeeCompensation.setEmployee(createdEmployee);
        employeeCompensation.setEffectiveDate(new Date());
        employeeCompensation.setSalary(java.math.BigDecimal.valueOf(76000.89));

        Compensation createdComp = restTemplate.postForEntity(compensationPostUrl, employeeCompensation, Compensation.class).getBody();
        assertNotNull(createdComp.getSalary());

        Compensation readCompensation = restTemplate.getForEntity(compensationGetUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(readCompensation.getSalary(), java.math.BigDecimal.valueOf(76000.89));
        assertEquals(readCompensation.getEmployee().getEmployeeId(), createdEmployee.getEmployeeId());
    }

	private void assertEmployeeEquivalence(Employee expected, Employee actual) {
		// TODO Auto-generated method stub
		assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
	}

}
