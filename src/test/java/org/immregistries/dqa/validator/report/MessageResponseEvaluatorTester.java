package org.immregistries.dqa.validator.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.DqaMessageServiceResponse;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.detection.ValidationReport;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.rules.patient.PatientExists;
import org.immregistries.dqa.validator.engine.rules.patient.PatientNameIsValid;
import org.immregistries.dqa.validator.engine.rules.vaccination.VaccinationIsPresent;
import org.immregistries.dqa.vxu.VxuObject;
import org.junit.Test;

public class MessageResponseEvaluatorTester {

  @Test
  public void test() {
    MessageResponseEvaluator eval = MessageResponseEvaluator.INSTANCE;
    DqaMessageServiceResponse validationResults = new DqaMessageServiceResponse();
    validationResults.setValidationResults(generateTestSubject());
    DqaMessageMetrics metrics = eval.toMetrics(validationResults);
    assertNotNull(metrics);
    assertEquals("should have the right number of object types represented", 4,
        metrics.getObjectCounts().size());

    assertEquals("Should have one patient: ", new Integer(1), metrics.getObjectCounts().get(
        VxuObject.PATIENT));
    assertEquals("Should have three vaccination: ", new Integer(3), metrics.getObjectCounts().get(
        VxuObject.VACCINATION));
    assertEquals("Should have zero of NOK: ", new Integer(0), metrics.getObjectCounts().get(
        VxuObject.NEXT_OF_KIN));
    assertEquals("Should have a count for patient middle name is misisng: ", new Integer(1),
        metrics.getAttributeCounts().get(Detection.PatientMiddleNameIsMissing));
  }

  public List<ValidationRuleResult> generateTestSubject() {

    List<ValidationRuleResult> results = new ArrayList<>();

    ValidationRuleResult result = new ValidationRuleResult();
    result.setRuleClass(PatientExists.class);
    result.setValidationDetections(new ArrayList<ValidationReport>());
    result.setRulePassed(true);
    results.add(result);

    ValidationRuleResult result2 = new ValidationRuleResult();
    result2.setRuleClass(VaccinationIsPresent.class);
    result2.setValidationDetections(new ArrayList<ValidationReport>());
    result2.setRulePassed(true);
    results.add(result2);

    ValidationRuleResult result21 = new ValidationRuleResult();
    result21.setRuleClass(VaccinationIsPresent.class);
    result21.setValidationDetections(new ArrayList<ValidationReport>());
    result21.setRulePassed(true);
    results.add(result21);

    ValidationRuleResult result22 = new ValidationRuleResult();
    result22.setRuleClass(VaccinationIsPresent.class);
    result22.setValidationDetections(new ArrayList<ValidationReport>());
    result22.setRulePassed(true);
    results.add(result22);

    ValidationRuleResult result3 = new ValidationRuleResult();
    result3.setRuleClass(PatientNameIsValid.class);
    ValidationReport issue = new ValidationReport();
    issue.setDetection(Detection.PatientMiddleNameIsMissing);
    result3.getValidationDetections().add(issue);
    result3.setRulePassed(false);
    results.add(result3);

    return results;

  }

  private String testMessage =
      "MSH|^~\\&|||||20170301131228-0500||VXU^V04^VXU_V04|3WzJ-A.01.01.2aF|P|2.5.1|" +
          "\nPID|||3WzJ-A.01.01^^^AIRA-TEST^MR||McCracken^Vinvella^^^^^L|Butler^Pauline|20130225|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|81 Page Pl^^GR^MI^49544^USA^P||^PRN^PH^^^616^9245843|||||||||2135-2^Hispanic or Latino^HL70005|"
          +
          "\nPD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20170301|20170301|" +
          "\nNK1|1|McCracken^Pauline^^^^^L|MTH^Mother^HL70063|81 Page Pl^^GR^MI^49544^USA^P|^PRN^PH^^^616^9245843|"
          +
          "\nORC|RE||V51L2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|"
          +
          "\nRXA|0|1|20170301||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y5841RR||MSD^Merck and Co^MVX||||A|"
          +
          "\nRXR|SC^^HL70162|RA^^HL70163|" +
          "\nOBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20170301|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|"
          +
          "\nOBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|" +
          "\nOBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|" +
          "\nOBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20170301||||||F|";

}
