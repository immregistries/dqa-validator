package org.immregistries.mqe.validator;

import java.util.Calendar;
import java.util.List;
import org.immregistries.mqe.hl7util.SeverityLevel;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.MessageValidator;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.parse.HL7MessageParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationTester {

  private static final Logger logger = LoggerFactory.getLogger(ValidationTester.class);
  private HL7MessageParser parser = HL7MessageParser.INSTANCE;
  private MessageValidator validator = MessageValidator.INSTANCE;
  private MqeMessageService service = MqeMessageService.INSTANCE;

  TestMessageGenerator genr = new TestMessageGenerator();

  @Test
  public void test1() {
    validateAndReport(genr.getAiraTestMsg());
  }

  @Test
  public void test12() {
    validateAndReport(genr.getExampleVXU_1());
  }

  @Test
  public void test2() {
    validateAndReport(genr.getExampleVXU_2());
  }

  @Test
  public void test3() {
    validateAndReport(genr.getAiraTestMsg());
  }

  @Test
  public void test4() {
    validateAndReport(genr.getExampleVXU_3());
  }
  
  @Test
  public void testBadLotNumber()
  {
    parseValidateAndReport(genr.getBadLotNumberExample());
  }

  private void validateAndReport(String message) {
    System.out.println("MESSAGE: ***********************************************");
    String[] lines = message.split("\\r");
    for (String line : lines) {
      System.out.println("         " + line);
    }
    System.out.println("********************************************************");
    MqeMessageReceived mr = parser.extractMessageFromText(message);

    long start = Calendar.getInstance().getTimeInMillis();
    List<ValidationRuleResult> list = validator.validateMessage(mr);
    long finish = Calendar.getInstance().getTimeInMillis();

    System.out.println("IT TOOK: " + (finish - start) + " ms to validate");

    System.out.println("ACCEPT: ***********************************************");
    reportAcceptResults(list);
    System.out.println("WARN  : ***********************************************");
    reportWarnResults(list);
    System.out.println("ERROR : ***********************************************");
    reportErrorResults(list);
  }
  
  private void parseValidateAndReport(String message) {
    System.out.println("MESSAGE: ***********************************************");
    String[] lines = message.split("\\r");
    for (String line : lines) {
      System.out.println("         " + line);
    }
    System.out.println("********************************************************");
    MqeMessageReceived mr = 
    service.extractMessageFromText(message);

    long start = Calendar.getInstance().getTimeInMillis();
    List<ValidationRuleResult> list = validator.validateMessage(mr);
    long finish = Calendar.getInstance().getTimeInMillis();

    System.out.println("IT TOOK: " + (finish - start) + " ms to validate");

    System.out.println("ACCEPT: ***********************************************");
    reportAcceptResults(list);
    System.out.println("WARN  : ***********************************************");
    reportWarnResults(list);
    System.out.println("ERROR : ***********************************************");
    reportErrorResults(list);
  }

  private void reportErrorResults(List<ValidationRuleResult> list) {
    reportResults(list, SeverityLevel.ERROR);
  }

  private void reportAcceptResults(List<ValidationRuleResult> list) {
    reportResults(list, SeverityLevel.ACCEPT);
  }

  private void reportWarnResults(List<ValidationRuleResult> list) {
    reportResults(list, SeverityLevel.WARN);
  }

  private void reportResults(List<ValidationRuleResult> list, SeverityLevel a) {
    for (ValidationRuleResult vrr : list) {
      for (ValidationReport i : vrr.getValidationDetections()) {
        if (a == i.getSeverity()) {
          String s = "  - ";
          if (i.getHl7LocationList() != null && i.getHl7LocationList().size() > 0) {
            s += i.getHl7LocationList().get(0);
          }
          s += "                   ";
          if (s.length() > 10) {
            s = s.substring(0, 18);
          }
          System.out.println(s + ": " + i.getDetection() + "[" + i.getValueReceived() + "]");
        }
      }
    }
  }

}
