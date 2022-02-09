package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private Employee employeeTest;
    private String reportingStructureURL;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureURL = "http://localhost:" + port + "reportingStructure/{id}";
        createEmployees();
    }

    private void createEmployees() {

        Employee directReport1 = new Employee();
        directReport1.setFirstName("John");
        directReport1.setLastName("Doe");

        Employee directReport2 = new Employee();
        directReport2.setFirstName("Ryan");
        directReport2.setLastName("Smith");
        List<Employee> directReportList1 = new ArrayList<>();
        directReportList1.add(directReport1);
        directReport2.setDirectReports(directReportList1);

        employeeTest = new Employee();
        employeeTest.setFirstName("Lorem");
        employeeTest.setLastName("Ipsam");
        List<Employee> directReportList2 = new ArrayList<>();
        directReportList2.add(directReport2);
        employeeTest.setDirectReports(directReportList2);

    }

    @Test
    public void numberOfReportsTest() {
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employeeTest, Employee.class).getBody();
        int expected = 2;
        int result = restTemplate.getForEntity(reportingStructureURL, Integer.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(expected, result);
    }


    @Test
    public void numberOfReportsExceptionTest() {
        int expected = -1;
        int result = restTemplate.getForEntity(reportingStructureURL, Integer.class, "123").getBody();
        assertEquals(expected, result);
    }
}
