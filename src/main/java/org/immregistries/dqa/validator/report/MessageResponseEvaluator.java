package org.immregistries.dqa.validator.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.dqa.validator.DqaMessageServiceResponse;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.validator.detection.ValidationReport;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.rules.nextofkin.NextOfKinIsPresent;
import org.immregistries.dqa.validator.engine.rules.vaccination.VaccinationIsPresent;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.VxuObject;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MessageResponseEvaluator {
                                      INSTANCE;
  private static final Logger logger = LoggerFactory.getLogger(MessageResponseEvaluator.class);

  public DqaMessageMetrics toMetrics(DqaMessageServiceResponse validationResults) {
    List<ValidationRuleResult> ruleResults = validationResults.getValidationResults();
    DqaMessageMetrics metrics = new DqaMessageMetrics();
    Map<Detection, Integer> attributeCounts = makeCountMap(ruleResults);
    metrics.setAttributeCounts(attributeCounts);
    metrics.getObjectCounts().putAll(makeObjectCounts(ruleResults));
    metrics.getPatientAgeCounts().put(makePatientAgeCounts(validationResults), 1);
    logger.info(metrics.toString());
    return metrics;
  }

  protected Map<VxuObject, Integer> makeObjectCounts(List<ValidationRuleResult> results) {
    Map<VxuObject, Integer> objCounts = new HashMap<VxuObject, Integer>();

    objCounts.put(VxuObject.PATIENT, 1);
    objCounts.put(VxuObject.MESSAGE_HEADER, 1);

    int vaccCount = 0;
    int nokCount = 0;

    for (ValidationRuleResult result : results) {

      if (result.getRuleClass().equals(VaccinationIsPresent.class)) {
        if (result.isRulePassed()) {
          vaccCount++;
        }
      }

      if (result.getRuleClass().equals(NextOfKinIsPresent.class)) {
        if (result.isRulePassed()) {
          nokCount++;
        }
      }

    }

    objCounts.put(VxuObject.VACCINATION, vaccCount);
    objCounts.put(VxuObject.NEXT_OF_KIN, nokCount);
    return objCounts;
  }

  protected Integer makePatientAgeCounts(
      DqaMessageServiceResponse validationResults) {

    DqaPatient patient = validationResults.getMessageObjects().getPatient();
    if (patient.getBirthDate() != null
        && validationResults.getMessageObjects().getReceivedDate() != null) {
      LocalDate birthDate = new LocalDate(patient.getBirthDate());
      LocalDate recDate = new LocalDate(validationResults.getMessageObjects().getReceivedDate());
      Period period = new Period(recDate, birthDate);
      Integer age = new Integer(Math.abs(period.getYears()));
      return age;
    }
    return -1;
  }

  protected Map<Detection, Integer> makeCountMap(List<ValidationRuleResult> results) {
    Map<Detection, Integer> map = new HashMap<>();
    for (ValidationRuleResult result : results) {
      List<ValidationReport> issues = result.getValidationDetections();
      for (ValidationReport issue : issues) {
        Detection attr = issue.getDetection();
        Integer cnt = map.get(attr);
        if (cnt == null) {
          map.put(attr, 1);
        } else {
          map.put(attr, cnt + 1);
        }
      }
    }

    return map;
  }
}
