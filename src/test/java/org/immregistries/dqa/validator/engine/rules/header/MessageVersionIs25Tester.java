package org.immregistries.dqa.validator.engine.rules.header;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by KDavis on 5/7/2018.
 */
public class MessageVersionIs25Tester {
  
  private MessageVersionIs25 rule = new MessageVersionIs25();
  
  // Parts required for the test
  private DqaMessageHeader mh = new DqaMessageHeader();
  private DqaMessageReceived mr = new DqaMessageReceived();
  
  private static final Logger logger = LoggerFactory.getLogger(MessageVersionIs25Tester.class);

  @Test
  public void testRule25() {
    mh.setMessageVersion("2.5");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }
  
  @Test
  public void testRule25Long() {
    mh.setMessageVersion("2.5.3");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }
  
  @Test
  public void testRule25Swap() {
    mh.setMessageVersion("5.2");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
  
  @Test
  public void testRuleNull() {
    mh.setMessageVersion(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
  
  
  @Test
  public void testRuleEmpty() {
    mh.setMessageVersion(" ");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
  
  @Test
  public void testRuleNot25() {
    mh.setMessageVersion("3.1");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
  
  @Test
  public void testRuleNotNumber() {
    mh.setMessageVersion("abc");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
}
