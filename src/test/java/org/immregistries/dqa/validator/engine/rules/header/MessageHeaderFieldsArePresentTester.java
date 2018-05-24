package org.immregistries.dqa.validator.engine.rules.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.vxu.DqaAddress;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHeaderFieldsArePresentTester {
  private MessageHeaderFieldsArePresent rule = new MessageHeaderFieldsArePresent();
  
  // Parts required for the test
  private DqaMessageHeader mh = new DqaMessageHeader();
  private DqaMessageReceived mr = new DqaMessageReceived();
  
  private static final Logger logger = LoggerFactory.getLogger(MessageHeaderFieldsArePresentTester.class);

  @Before
  public void setUpTheHeader() {
    mh.setReceivingApplication("ReceivingApp");
    mh.setSendingApplication("SendingApp");
    mh.setReceivingFacility("ReceivingFacility");
    mh.setMessageControl("MessageControl");
    mh.setAckTypeAcceptCode("AckAcceptCode");
    mh.setAckTypeApplicationCode("AckAppCode");
  }
  
  @Test
  public void testRule() {
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertTrue(r.isRulePassed());
  }  
  
  @Test
  public void testRuleNoReceiveApp() {
    mh.setReceivingApplication(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageReceivingApplicationIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  @Test
  public void testRuleNoReceiveFacility() {
    mh.setReceivingFacility(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageReceivingFacilityIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  @Test
  public void testRuleNoSendingApp() {
    mh.setSendingApplication(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageSendingApplicationIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  @Test
  public void testRuleNoMsgControl() {
    mh.setMessageControl(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageMessageControlIdIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  @Test
  public void testRuleNoAckAcceptCode() {
    mh.setAckTypeAcceptCode(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageAcceptAckTypeIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  
  @Test
  public void testRuleNoAckAppCode() {
    mh.setAckTypeApplicationCode(null);
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
    assertEquals(Detection.MessageAppAckTypeIsMissing,
        r.getValidationDetections().get(0).getDetection());
  }  
  
  @Test
  public void testRuleNoFields() {
    mh.setReceivingApplication("");
    mh.setSendingApplication("");
    mh.setReceivingFacility("");
    mh.setMessageControl("");
    mh.setAckTypeAcceptCode("");
    mh.setAckTypeApplicationCode("");
    ValidationRuleResult r = rule.executeRule(mh, mr);
    logger.info(r.getValidationDetections().toString());
    assertFalse(r.isRulePassed());
  } 
}
