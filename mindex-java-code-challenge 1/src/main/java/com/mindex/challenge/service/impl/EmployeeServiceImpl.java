package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

	@Override
	public ReportingStructure getReportingStructure(String id) {
		LOG.debug("Building report structure for id [{}]", id);
		
		List<Employee> tempList = new ArrayList<>();
		int numberOfReports = 0;
		Employee employee = employeeRepository.findByEmployeeId(id);
		if (CollectionUtils.isEmpty(employee.getDirectReports())) {
			ReportingStructure reportingStructure = new ReportingStructure();
			reportingStructure.setEmployee(employee);
			reportingStructure.setNumberOfReports(0);
			return reportingStructure;
		}
		tempList.add(employee);
		while (!CollectionUtils.isEmpty(tempList)) {
			Employee currentEmployee = tempList.get(0);
			tempList.remove(0);
			List<Employee> currentEmployeeDirectReports = currentEmployee.getDirectReports();
			if (CollectionUtils.isEmpty(currentEmployeeDirectReports)) {
				continue;
			}
			for (Employee e : currentEmployeeDirectReports) {
				numberOfReports++;
				Employee tempEmployee = employeeRepository.findByEmployeeId(e.getEmployeeId());
				tempList.add(tempEmployee);
			}
		}
		ReportingStructure reportingStructure = new ReportingStructure();
		reportingStructure.setEmployee(employee);
		reportingStructure.setNumberOfReports(numberOfReports);
		return reportingStructure;
	}
}
