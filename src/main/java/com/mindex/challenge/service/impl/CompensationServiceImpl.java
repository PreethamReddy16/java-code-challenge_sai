package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation: [{}]", compensation);
        return compensationRepository.insert(compensation);
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with employeeId: [{}]", id);
        Employee employee = null;
        try {
            employee = employeeService.read(id);
        } catch (RuntimeException e) {
            return new Compensation();
        }
        Compensation compensation = compensationRepository.findByEmployee(employee);

        if (compensation == null) {
            return new Compensation();
        }
        return compensation;
    }
}
