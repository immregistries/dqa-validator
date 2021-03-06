package org.immregistries.mqe.validator.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.mqe.util.validation.MqeDetection;
import org.immregistries.mqe.validator.MqeMessageServiceResponse;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.validator.engine.rules.nextofkin.NextOfKinIsPresent;
import org.immregistries.mqe.validator.engine.rules.vaccination.VaccinationIsPresent;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.VxuObject;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MessageResponseEvaluator {
                                      INSTANCE;
  private static final Logger logger = LoggerFactory.getLogger(MessageResponseEvaluator.class);

  public MqeMessageMetrics toMetrics(MqeMessageServiceResponse validationResults) {
    List<ValidationRuleResult> ruleResults = validationResults.getValidationResults();
    MqeMessageMetrics metrics = new MqeMessageMetrics();
    Map<MqeDetection, Integer> attributeCounts = makeCountMap(ruleResults);
    metrics.setAttributeCounts(attributeCounts);
    metrics.getObjectCounts().putAll(makeObjectCounts(ruleResults));
    metrics.getPatientAgeCounts().put(getPatientAgeForMessage(validationResults), 1);
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

  protected Integer getPatientAgeForMessage(
      MqeMessageServiceResponse validationResults) {

    if (validationResults != null && validationResults.getMessageObjects() != null) {
      MqePatient patient = validationResults.getMessageObjects().getPatient();
      if (patient.getBirthDate() != null
          && validationResults.getMessageObjects().getReceivedDate() != null) {
        LocalDate birthDate = new LocalDate(patient.getBirthDate());
        LocalDate recDate = new LocalDate(validationResults.getMessageObjects().getReceivedDate());
        Period period = new Period(recDate, birthDate);
        Integer age = new Integer(Math.abs(period.getYears()));
        return age;
      }
    }
    return -1;//we don't have an age to report.
  }

  protected Map<MqeDetection, Integer> makeCountMap(List<ValidationRuleResult> results) {
    Map<MqeDetection, Integer> map = new HashMap<>();
    for (ValidationRuleResult result : results) {
      List<ValidationReport> issues = result.getValidationDetections();
      for (ValidationReport issue : issues) {
        MqeDetection attr = issue.getDetection();
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
