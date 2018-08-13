package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.immregistries.codebase.client.generated.Code;
import org.immregistries.codebase.client.reference.CodesetType;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.immregistries.mqe.vxu.VxuField;

public class VaccinationAdminDateIsValidForPatientAge extends ValidationRule<MqeVaccination> {


  @Override
  protected final Class[] getDependencies() {
    return new Class[] {VaccinationCvxIsValid.class};
  }

  public VaccinationAdminDateIsValidForPatientAge() {
    ruleDetections.addAll(codr.getDetectionsForField(VxuField.VACCINATION_ADMIN_DATE));
  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {
    LOGGER.info("VaccinationAdminDateIsValidForPatientAge - evaluating");
    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    String cvxCode = target.getAdminCvxCode();
    Code vaccineCode = this.repo.getCodeFromValue(cvxCode, CodesetType.VACCINATION_CVX_CODE);
    LOGGER.info("VaccinationAdminDateIsValidForPatientAge - CVX: " + cvxCode);
    LOGGER.info("VaccinationAdminDateIsValidForPatientAge - Admin Date "
        + target.getAdminDateString());
    LOGGER.info("VaccinationAdminDateIsValidForPatientAge - Birth Date "
        + m.getPatient().getBirthDateString());
    if (vaccineCode != null && !this.common.isEmpty(target.getAdminDateString())) {

      Date adminDate = datr.parseDate(target.getAdminDateString());
      Date birthDate = datr.parseDate(m.getPatient().getBirthDateString());

      if (birthDate != null) {
        issues.addAll(codr.handleAgeDate(vaccineCode, adminDate, birthDate,
            VxuField.VACCINATION_ADMIN_DATE, target));
      }

      passed = (issues.size() == 0);
    }

    return buildResults(issues, passed);
  }
}