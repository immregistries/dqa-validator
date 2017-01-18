package org.immregistries.dqa.validator.engine.rules.patient;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.codes.KnowNameList;
import org.immregistries.dqa.validator.engine.codes.model.KnownName;
import org.immregistries.dqa.validator.engine.codes.model.KnownName.NameType;
import org.immregistries.dqa.validator.engine.issues.MessageAttribute;
import org.immregistries.dqa.validator.engine.issues.ValidationIssue;
import org.immregistries.dqa.validator.model.DqaMessageReceived;
import org.immregistries.dqa.validator.model.DqaPatient;

public class PatientMiddleNameIsValid extends ValidationRule<DqaPatient> {

	@Override
	protected ValidationRuleResult executeRule(DqaPatient target, DqaMessageReceived m) {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = true;
		
		List<KnownName> invalidNames = KnowNameList.INSTANCE.getKnownNames(NameType.INVALID_NAME);
		
		String middleName = target.getNameMiddle();
		  
		if (common.isEmpty(middleName)) {
			issues.add(MessageAttribute.PatientMiddleNameIsMissing.build());
			passed = false;
		} else {
			for (KnownName invalidName : invalidNames) {
				if (invalidName.onlyNameMiddle()
						&& middleName.equalsIgnoreCase(invalidName.getNameMiddle())) {
					
					issues.add(MessageAttribute.PatientMiddleNameIsInvalid.build());
					break;// this gets out of the for loop.
				}
			}

			if (middleName.length() == 1) {
				issues.add(MessageAttribute.PatientMiddleNameMayBeInitial.build());
			}

			if (middleName.endsWith(".")) {// why are we removing dots???
				String moddedMiddle = middleName.substring(0, middleName.length() - 1);
	
				if (!common.isValidNameChars(moddedMiddle)) {
					issues.add(MessageAttribute.PatientMiddleNameIsInvalid.build());
					passed = false;
				}
			}
		} 
		return buildResults(issues, passed);
	}
	
	

}