package org.immregistries.mqe.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.hl7.PatientIdNumber;

public class PatientMedicaidNumberIsValid extends ValidationRule<MqePatient> {

  // Because of this, we'll skip this rule if there is no patient object.
  @Override
  protected final Class[] getDependencies() {
    return new Class[] {PatientExists.class};
  }

  public PatientMedicaidNumberIsValid() {
    ruleDetections.addAll(Arrays.asList(Detection.PatientMedicaidNumberIsMissing,
        Detection.PatientMedicaidNumberIsInvalid));
  }

  /*
   * This is the money:
   */
  @Override
  protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    // what should I be doing when the target is empty... no issues, and no pass for this guy I
    // guess...
    // Could just bypass this by requiring the patient to exist.
    if (target == null) {
      return buildResults(issues, false);
    }

    PatientIdNumber id = target.getIdMedicaid();

    if (id == null || this.common.isEmpty(id.getNumber())) {
      issues.add(Detection.PatientMedicaidNumberIsMissing.build(target));
      passed = false;
    } else if (!common.isValidIdentifier(id.getNumber(), 9)) {
      issues.add(Detection.PatientMedicaidNumberIsInvalid.build(id.getNumber(), target));
      passed = false;
    }

    return buildResults(issues, passed);
  }
}