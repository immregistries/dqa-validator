package org.immregistries.dqa.validator.engine.rules.header;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHeaderCodesAreValidTester {
  
  private MessageHeaderCodesAreValid rule = new MessageHeaderCodesAreValid();
  
  // Parts required for the test
  private DqaMessageHeader mh = new DqaMessageHeader();
  private DqaMessageReceived mr = new DqaMessageReceived();
  
  private static final Logger logger = LoggerFactory.getLogger(MessageHeaderCodesAreValidTester.class);
  
  /* Probably should improve CodeCollection class so we can use the defined codes without needing a message*/
  private static String ALWAYS = "AL";
  private static String NEVER = "NE";
  private static String ERROR = "ER";
  private static String SUCCESS  = "SU";
  
  @Test
  public void testRule() {
    mh.setAckTypeAcceptCode(ALWAYS);
    mh.setAckTypeApplicationCode(ERROR);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }
  
  @Test
  public void testRuleNoAccept() {
    mh.setAckTypeAcceptCode(null);
    mh.setAckTypeApplicationCode(SUCCESS);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
  
  @Test
  public void testRuleNoApp() {
    mh.setAckTypeAcceptCode(NEVER);
    mh.setAckTypeApplicationCode(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  }
}
