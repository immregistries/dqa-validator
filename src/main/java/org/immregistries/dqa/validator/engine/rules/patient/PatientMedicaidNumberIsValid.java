package org.immregistries.dqa.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.Detection;
import org.immregistries.dqa.validator.issue.ValidationIssue;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.hl7.PatientIdNumber;

public class PatientMedicaidNumberIsValid extends ValidationRule<DqaPatient> {

	//Because of this, we'll skip this rule if there is no patient object. 
	@Override
	protected final Class[] getDependencies() {
		return new Class[] {PatientExists.class};
	}
	
	
	/*
	 * This is the money: 
	 */
	@Override
	protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
		
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = true;
		
		//what should I be doing when the target is empty...  no issues, and no pass for this guy I guess...
		//Could just bypass this by requiring the patient to exist. 
		if (target == null) {
			return buildResults(issues, false);
		}
		
		PatientIdNumber id = target.getIdMedicaid();
		
	    if (id == null || common.isEmpty(id.getNumber())) {
	    	issues.add(Detection.PatientMedicaidNumberIsMissing.build());
	    	passed = false;
	    } else if (!common.isValidIdentifier(id.getNumber(), 9)) {
	      issues.add(Detection.PatientMedicaidNumberIsInvalid.build(id.getNumber()));
	      passed = false;
	    }

		return buildResults(issues, passed);
	}
}
