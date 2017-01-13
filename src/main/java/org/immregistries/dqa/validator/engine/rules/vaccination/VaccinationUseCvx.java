package org.immregistries.dqa.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.codebase.client.generated.Code;
import org.immregistries.dqa.codebase.client.reference.CodeStatusValue;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.issues.ValidationIssue;
import org.immregistries.dqa.validator.model.DqaMessageReceived;
import org.immregistries.dqa.validator.model.DqaVaccination;

public class VaccinationUseCvx extends ValidationRule<DqaVaccination> {

	@Override
	protected ValidationRuleResult executeRule(DqaVaccination target,
			DqaMessageReceived m) {

		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = true;

		String cvxString = target.getAdminCvxCode();
		
		Code cvxCode = repo.getCodeFromValue(cvxString, CodesetType.VACCINATION_CVX_CODE);
		CodeStatusValue cvxStatus = CodeStatusValue.getBy(cvxCode.getCodeStatus());
		
		boolean useCvx = true;
		
		if (cvxStatus == null || cvxCode == null) {
			useCvx = false;
		}
		
		if (useCvx) {
			switch (cvxStatus) {
			case INVALID:
			case IGNORED:
				useCvx = true;
				break;
			default:
				break;
			}
		}
		
		passed = useCvx;

		return buildResults(issues, passed);
	}
}
