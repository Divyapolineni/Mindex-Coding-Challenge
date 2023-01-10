package com.mindex.challenge.service;

import java.util.List;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
	Compensation getCompensationByEmployeeId(String employeeId);
	Object createCompensation(Compensation compensation);
	Compensation getCompensationByCompensationId(String compensationId);
	List<Compensation> getAllCompensations();
}
