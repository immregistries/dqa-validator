package org.immregistries.dqa.validator.engine.rules.vaccination;

import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Allison on 5/9/2017.
 */
public class ObservationValueTypeIsValidTester {
    private ObservationValueTypeIsValid rule = new ObservationValueTypeIsValid();

    // Parts required for the test
    private DqaMessageHeader mh = new DqaMessageHeader();
    private DqaMessageReceived mr = new DqaMessageReceived();
    private DqaVaccination v = new DqaVaccination();
    private Observation o = new Observation();

    private static final Logger logger = LoggerFactory.getLogger(ObservationValueTypeIsValidTester.class);

    /**
     * Sets up the objects needed for the test.
     */
    @Before
    public void setUpTheObjects() {
        o.setValueTypeCode("CE"); // CE = Coded Entry

        mh.setMessageDate(new Date());
        mr.setMessageHeader(mh);
        setObservationsAndVaccinations();
    }

    /**
     * Test the basic rule with a valid type code.
     * (should be true)
     */
    @Test
    public void testRule() {
        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(r.isRulePassed());
    }

    /**
     * Test the rule with a null type code.
     */
    @Test
    public void testRuleNullType() {
        o.setValueTypeCode(null);
        setObservationsAndVaccinations();

        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.ObservationValueTypeIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Test the rule with an empty type code.
     */
    @Test
    public void testRuleEmptyType() {
        o.setValueTypeCode("");
        setObservationsAndVaccinations();

        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.ObservationValueTypeIsMissing == r.getIssues().get(0).getIssue());
    }

    /**
     * Test the rule with an unrecognized type code.
     */
    @Test
    public void testRuleUnrecognizedType() {
        o.setValueTypeCode("abc");
        setObservationsAndVaccinations();

        ValidationRuleResult r = rule.executeRule(v, mr);
        logger.info(r.getIssues().toString());
        assertTrue(1 == r.getIssues().size()
                && Detection.ObservationValueTypeIsUnrecognized == r.getIssues().get(0).getIssue());
    }

    /**
     * Set the observation/vaccination we're currently looking at.
     */
    private void setObservationsAndVaccinations() {
        List<Observation> obs = new ArrayList<>();
        obs.add(o);
        v.setObservations(obs);
    }
}
