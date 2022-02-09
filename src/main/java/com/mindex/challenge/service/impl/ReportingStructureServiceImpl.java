package com.mindex.challenge.service.impl;

import com.mindex.challenge.controller.ReportingStructureController;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService{
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public int numberOfReports(String id) {
        LOG.debug("no.of reports with id [{}]", id);
        int result = 0;
        //direct reports count through DFS

        //Tto get unique direct reports using set
        Set<Employee> isPresent = new HashSet<>();
        Employee employee = null;
        try {
            employee = employeeService.read(id);
        } catch (RuntimeException ex) {
            return -1;
        }
        /// DFS
        Stack<Employee> stack = new Stack<>();
        stack.push(employee);

        while(!stack.isEmpty()){
            Employee currentEmployee = stack.pop();
            isPresent.add(currentEmployee);
            List<Employee> directRreports = currentEmployee.getDirectReports();
            if(directRreports !=null &&  !directRreports.isEmpty()){
                result += directRreports.size();
                for (Employee dirEmp: directRreports) {
                    if (!isPresent.contains(dirEmp)) {
                        stack.push(dirEmp);
                    }
                }
            }
        }

        return result;
    }

}
