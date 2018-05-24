package org.immregistries.dqa.validator.engine.rules.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageVersionIsValidTester {
  private MessageVersionIsValid rule = new MessageVersionIsValid();
  
  // Parts required for the test
  private DqaMessageHeader mh = new DqaMessageHeader();
  private DqaMessageReceived mr = new DqaMessageReceived();
  
  private static final Logger logger = LoggerFactory.getLogger(MessageVersionIsValidTester.class);

  @Test
  public void testRuleValid() {
    mh.setMessageVersion("2.5.1");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }  
  
  @Test
  public void testRuleNull() {
    mh.setMessageVersion(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageVersionIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }
  
  
  @Test
  public void testRuleEmpty() {
    mh.setMessageVersion(" ");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageVersionIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }
  
  @Test
  public void testRuleUnrecognized() {
    mh.setMessageVersion("abc");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageVersionIsUnrecognized,
        r.getValidationDetections().get(0).getDetection());
  }
}
