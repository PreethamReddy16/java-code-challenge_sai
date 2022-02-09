package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String createCompensationUrl;
    private String readCompensationUrl;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Employee employeeTest;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        createCompensationUrl = "http://localhost:" + port + "/compensation";
        readCompensationUrl = "http://localhost:" + port + "/compensation/{id}";

        employeeTest = employeeRepository.findByEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
    }

    @After
    public void teardown() {
        employeeUrl = null;
        createCompensationUrl = null;
        readCompensationUrl = null;
        employeeTest = null;
    }

    @Test
    public void testReportingStructureCreateRead() {
        Compensation testComp = new Compensation();
        testComp.setEmployee(employeeTest);
        testComp.setSalary("1234");
        testComp.setEffectiveDate(Instant.parse("2022-02-09T11:10:00Z"));

        // CreateComp test
        ResponseEntity createdCompResponse = restTemplate.postForEntity(createCompensationUrl, testComp, Compensation.class);
        assertEquals(HttpStatus.OK, createdCompResponse.getStatusCode());
        Compensation createdComp = (Compensation) createdCompResponse.getBody();
        assertNotNull(createdComp);
        assertCompEquivalence(testComp, createdComp);

        //ReadComp test
        ResponseEntity readCompResponse = restTemplate.getForEntity(readCompensationUrl, Compensation.class, createdComp.getEmployee().getEmployeeId());
        assertEquals(HttpStatus.OK, readCompResponse.getStatusCode());
        Compensation readComp = (Compensation) readCompResponse.getBody();
        assertNotNull(readComp);
        assertCompEquivalence(readComp, createdComp);
    }

    private static  void assertCompEquivalence(Compensation expected, Compensation actual){
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
