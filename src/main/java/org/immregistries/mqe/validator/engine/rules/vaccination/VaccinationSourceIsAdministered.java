package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;

public class VaccinationSourceIsAdministered extends ValidationRule<MqeVaccination> {

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<>();
    boolean passed;

    String sourceCd = target.getInformationSourceCode();

    switch (sourceCd) {
	    case MqeVaccination.INFO_SOURCE_ADMIN:
	    	passed = true;
	      break;
	    default:
            passed = false;
	      break;
    }

    return buildResults(issues, passed);

  }
}
