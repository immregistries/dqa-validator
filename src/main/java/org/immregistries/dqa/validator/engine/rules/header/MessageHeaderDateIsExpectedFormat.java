package org.immregistries.dqa.validator.engine.rules.header;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.issue.MessageAttribute;
import org.immregistries.dqa.validator.issue.ValidationIssue;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MessageHeaderDateIsExpectedFormat extends ValidationRule<DqaMessageHeader> {

	private final String expected = "yyyyMMddHHmmssZ";
	private final DateTimeFormatter expectedFormat = DateTimeFormat.forPattern(expected);

	@Override
	protected final Class[] getDependencies() {
		return new Class[] {
		// PatientExists.class,
		};
	}

	@Override
	protected ValidationRuleResult executeRule(DqaMessageHeader target,
			DqaMessageReceived mr) {

		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		boolean passed = true;

		if (!common.isEmpty(target.getMessageDateString())) {
			if (!datr.isExpectedDateFormat(target.getMessageDateString(), expectedFormat)) {
				issues.add(MessageAttribute.MessageMessageDateIsUnexpectedFormat.build(target.getMessageDateString()));
			}
		}
		
		passed = issues.isEmpty();

		return buildResults(issues, passed);
	}

}