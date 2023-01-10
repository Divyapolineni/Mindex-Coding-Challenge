package com.mindex.challenge.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
	

    @Autowired
    private CompensationRepository compensationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

	@Override
	public Compensation getCompensationByEmployeeId(String employeeId) {
		return compensationRepository.findCompensationByEmployeeEmployeeId(employeeId);
	}

	@Override
	public Object createCompensation(Compensation compensation) {
		// TODO Auto-generated method stub
        LOG.debug("Creating compensation [{}]", compensation);
        
        String employeeId = compensation.getEmployee().getEmployeeId();
        if (!StringUtils.isEmpty(employeeId)) {
        	Employee employee = employeeRepository.findByEmployeeId(employeeId);
        	if (ObjectUtils.isEmpty(employee)) {
        		LOG.error("Could not create compensation since employee does not exist");
        		return "Could not create compensation since employee does not exist";
        	} else {
        		compensation.setCompensationId(UUID.randomUUID().toString());
        		compensationRepository.insert(compensation);
        	}
        }
        return compensation;
	}

	@Override
	public Compensation getCompensationByCompensationId(String compensationId) {
		Optional<Compensation> compensation = compensationRepository.findById(compensationId);
		if (compensation.isPresent()) {
			return compensation.get();
		} else {
			return null;
		}
	}

	@Override
	public List<Compensation> getAllCompensations() {
		return compensationRepository.findAll();
	}

}
