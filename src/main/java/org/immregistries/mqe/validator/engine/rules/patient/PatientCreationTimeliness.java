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
import org.immregistries.mqe.vxu.MqeVaccination;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

public class PatientCreationTimeliness extends ValidationRule<MqePatient> {

	@Override
	protected final Class[] getDependencies() {
		return new Class[] { PatientCreationDateIsValid.class, PatientBirthDateIsValid.class };
	}
	
	public PatientCreationTimeliness() {
		super();
		this.ruleDetections.addAll(Arrays.asList(
				Detection.PatientCreationIsVeryLate,
				Detection.PatientCreationIsTooLate,
				Detection.PatientCreationIsOnTime,
				Detection.PatientCreationIsLate
		));
	}

	@Override
	protected ValidationRuleResult executeRule(MqePatient target, MqeMessageReceived m) {
		List<ValidationReport> issues = new ArrayList<>();
		
		int w1 = 30;
		int w2 = 45;
		int w3 = 60;

		DateTime dob = new DateTime(target.getBirthDate());
		DateTime entry = new DateTime(target.getSystemEntryDate());
		Days difference = Days.daysBetween(dob.toLocalDate(), entry.toLocalDate());
		
		if(difference.getDays() <= w1){
			issues.add(Detection.PatientCreationIsOnTime.build(target.getSystemEntryDateString(), target));
		}
		else if(difference.getDays() > w1 && difference.getDays() <= w2){
			issues.add(Detection.PatientCreationIsLate.build(target.getSystemEntryDateString(), target));
		}
		else if(difference.getDays() > w2 && difference.getDays() <= w3){
			issues.add(Detection.PatientCreationIsVeryLate.build(target.getSystemEntryDateString(), target));
		}
		else if(difference.getDays() > w3){
			issues.add(Detection.PatientCreationIsTooLate.build(target.getSystemEntryDateString(), target));
		}
		
		return buildResults(issues, true);
	}

}