package org.immregistries.dqa.validator.engine.rules.patient;

import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaPatient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Allison on 5/9/2017.
 */
public class PatientMultipleBirthsValidTester {
    private PatientMultipleBirthsValid rule = new PatientMultipleBirthsValid();

    // Parts required for the test
    private DqaMessageHeader mh = new DqaMessageHeader();
    private DqaMessageReceived mr = new DqaMessageReceived();
    private DqaPatient p = new DqaPatient();

    private static final Logger logger = LoggerFactory.getLogger(PatientMultipleBirthsValidTester.class);

    /**
     * Sets up the objects needed for the test.
     */
    @Before
    public void setUpTheObjects() {
        p.setBirthMultipleIndicator("N");

        mh.setMessageDate(new Date());
        mr.setMessageHeader(mh);
        mr.setPatient(p);
    }

    /**
     * Test the basic rule, with a valid multiple birth indicator (N indicator + no birth order)
     * (should be true)
     */
    @Test
    public void testRule() {
        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(r.isRulePassed());
    }

    /**
     * Test the basic rule, with a valid multiple birth indicator (Y indicator + birth order)
     * (should be true)
     */
    @Test
    public void testRuleIfMultiple() {
        p.setBirthMultipleIndicator("Y");
        p.setBirthOrderNumber("2");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(r.isRulePassed());
    }

    /**
     * Test without multiple birth (N indicator) BUT with the birth order set to 1
     * (should be true)
     */
    @Test
    public void testRuleIfFirstChild() {
        p.setBirthOrderNumber("1");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(r.isRulePassed());
    }

    /**
     * Test with birth order but multiple birth indicator = N
     * (should be false)
     */
    @Test
    public void testRuleIfNoIndicator() {
        p.setBirthOrderNumber("2");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.PatientBirthOrderIsInvalid == r.getIssues().get(0).getIssue());
    }

    /**
     * Test with multiple birth indicator but no order
     * (should be false)
     */
    @Test
    public void testRuleIfNoOrder() {
        p.setBirthMultipleIndicator("Y");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(2 == r.getIssues().size());
        // should be both PatientBirthOrderIsMissing and PatientBirthOrderIsMissingAndMultipleBirthIndicated
    }

    /**
     * Test with birth order but null multiple birth indicator
     * (should be false)
     */
    @Test
    public void testRuleIfNullIndicator() {
        p.setBirthMultipleIndicator(null);

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.PatientBirthIndicatorIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Test with birth order but empty multiple birth indicator
     * (should be false)
     */
    @Test
    public void testRuleIfEmptyIndicator() {
        p.setBirthMultipleIndicator("");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.PatientBirthIndicatorIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Test with invalid multiple birth indicator
     * (should be false)
     */
    @Test
    public void testRuleIfInvalidIndicator() {
        p.setBirthMultipleIndicator("abc");
        p.setBirthOrderNumber("abc");

        ValidationRuleResult r = rule.executeRule(p, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.PatientBirthIndicatorIsInvalid == r.getIssues().get(0).getIssue());
    }

    /**
     * Test with invalid birth order number
     * (should be false)
     */
    @Test
    public void testRuleIfInvalidOrder() {
        p.setBirthMultipleIndicator("Y");
        p.setBirthOrderNumber("abc");

        ValidationRuleResult r = rule.executeRule(p, mr);
        assertEquals("should have one issue", 1, r.getIssues().size());
        assertEquals("the issue should be this one: ", Detection.PatientBirthOrderIsInvalid, r.getIssues().get(0).getIssue());
    }
}