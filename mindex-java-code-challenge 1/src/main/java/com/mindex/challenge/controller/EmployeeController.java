package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private CompensationService compensationService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
    
    @GetMapping("/employee/{id}/numberOfReports")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
    	LOG.debug("Received employee numberOfReports request for id [{}]", id);
    	
    	return employeeService.getReportingStructure(id);
    }
    
    @PostMapping("/employee/compensation")
    public ResponseEntity<?> createCompensation(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return new ResponseEntity<>(compensationService.createCompensation(compensation), HttpStatus.OK);
    }

    @GetMapping("/employee/compensation/{employeeId}")
    public Compensation getCompensation(@PathVariable String employeeId) {
        LOG.debug("Received get compensation request for id [{}]", employeeId);

        return compensationService.getCompensationByEmployeeId(employeeId);
    }
    
    @GetMapping("/employee/compensationByCompensationId/{compensationId}")
    public Compensation getCompensationByCompensationId(@PathVariable String compensationId) {
        LOG.debug("Received get compensation request for id [{}]", compensationId);

        return compensationService.getCompensationByCompensationId(compensationId);
    }
    
    
    @GetMapping("/employee/getAllCompensations")
    public List<Compensation> getAllCompensations() {
        return compensationService.getAllCompensations();
    }
}
