package org.immregistries.dqa.validator.engine.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.validator.engine.issues.IssueField;
import org.immregistries.dqa.validator.engine.issues.IssueType;
import org.immregistries.dqa.validator.engine.issues.MessageAttribute;
import org.immregistries.dqa.validator.engine.issues.ValidationIssue;
import org.immregistries.dqa.validator.model.hl7types.PhoneNumber;

/**
 * This is to evaluate the basic expectations for an address in the system this
 * does not guarantee that the address is real or that the street address is
 * properly formatted. this evaluates the very minimum required for the address
 * to be evaluated.
 * 
 * @author Josh
 *
 */
public enum PhoneValidator {
	INSTANCE;
	
	private CodeHandler coder = CodeHandler.INSTANCE;
	
	public List<ValidationIssue> validatePhone(PhoneNumber phone, IssueField piPhone) {
		return validatePhone(phone, piPhone, null, null);
	}

	public List<ValidationIssue> validatePhone(PhoneNumber phone,
			IssueField piPhone, 
			IssueField piPhoneTelUse,
			IssueField piPhoneTelEquip) {
		
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		
		if (StringUtils.isNotEmpty(phone.getNumber())) {
			
			if (phone.getAreaCode().equals("") || phone.getLocalNumber().equals("")) {
				MessageAttribute pIssue = MessageAttribute.get(piPhone, IssueType.INCOMPLETE);
				if (pIssue != null) {
					issues.add(pIssue.build(phone.getNumber()));
				}
			}
			
			//If there's a use code, make sure it's proper. 
			if (piPhoneTelUse != null) {
				issues.addAll(coder.handleCode(phone.getTelUse(), piPhoneTelUse));
			}
			
			//If it's got a code, make sure it's legit. 
			if (piPhoneTelEquip != null) {
				issues.addAll(coder.handleCode(phone.getTelEquip(), piPhoneTelEquip));
			}
			
			//Invalid phone number format. 
			if (!isValidPhone(phone)) {
				MessageAttribute pIssue = MessageAttribute.get(piPhone, IssueType.INVALID);
				if (pIssue != null) {
					issues.add(pIssue.build(phone.getNumber()));
				}
			}
			
		} else {
			issues.add(MessageAttribute.get(piPhone, IssueType.MISSING).build()); 
		}
		return issues;
	}

	protected boolean isValidPhone(PhoneNumber phone) {
		
		if (phone.getCountryCode().equals("")
				|| phone.getCountryCode().equals("1")
				|| phone.getCountryCode().equals("+1")) {
			// Validating all phone numbers using the North American Numbering
			// Plan
			// (NANP)
			
			if (!phone.getAreaCode().equals("")) {
				if (!validPhone3Digit(phone.getAreaCode())) {
					return false;
				}
			}
			
			if (!phone.getLocalNumber().equals("")) {
				String num = phone.getLocalNumber();
				StringBuilder numOnly = new StringBuilder();
				for (int i = 0; i < num.length(); i++) {
					if (num.charAt(i) >= '0' && num.charAt(i) <= '9') {
						numOnly.append(num.charAt(i));
					}
				}
				num = numOnly.toString();
				if (num.length() != 7) {
					return false;
				}
				if (!validPhone3Digit(num.substring(0, 3))) {
					return false;
				}
				if (num.substring(1, 3).equals("11")) {
					return false;
				}
			}
		}
		
		return true;
	}

	private static boolean validPhone3Digit(String s) {
		if (s == null || s.length() != 3) {
			return false;
		}
		if (s.charAt(0) < '2' || s.charAt(0) > '9') {
			return false;
		}
		if (s.charAt(1) < '0' || s.charAt(1) > '9') {
			return false;
		}
		if (s.charAt(2) < '0' || s.charAt(2) > '9') {
			return false;
		}
		return true;
	}

}