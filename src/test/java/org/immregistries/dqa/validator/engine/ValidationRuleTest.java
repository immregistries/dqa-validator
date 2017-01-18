package org.immregistries.dqa.validator.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.immregistries.dqa.validator.engine.ValidationRule;
import org.immregistries.dqa.validator.engine.ValidationRuleEntityLists;
import org.immregistries.dqa.validator.engine.ValidationRulePair;
import org.immregistries.dqa.validator.engine.ValidationRuleResult;
import org.immregistries.dqa.validator.engine.ValidationRunner;
import org.immregistries.dqa.validator.engine.ValidationUtility;
import org.immregistries.dqa.validator.engine.issues.ValidationIssue;
import org.immregistries.dqa.validator.engine.rules.patient.PatientBirthDateIsValid;
import org.immregistries.dqa.validator.engine.rules.patient.PatientExists;
import org.immregistries.dqa.validator.engine.rules.patient.PatientIsUnderage;
import org.immregistries.dqa.validator.engine.rules.vaccination.VaccinationAdminAfterBirthDate;
import org.immregistries.dqa.validator.engine.rules.vaccination.VaccinationAdminDateIsValid;
import org.immregistries.dqa.validator.model.DqaMessageReceived;
import org.immregistries.dqa.validator.model.DqaPatient;
import org.immregistries.dqa.validator.model.DqaVaccination;
import org.immregistries.dqa.validator.model.hl7types.Address;
import org.junit.Test;

public class ValidationRuleTest {

	private static final ValidationUtility util = ValidationUtility.INSTANCE;
	private static final ValidationRunner runner = ValidationRunner.INSTANCE;
	
	@Test
	public void whatClassDoesItReport() {
		ValidationRule<DqaVaccination> vr = new VaccinationAdminAfterBirthDate();
		assertEquals(VaccinationAdminAfterBirthDate.class,  vr.getClass());
	}
	
//	So..for a full fledged test, I'd need to parse a message into the model, and then invoke the validators. 
//  for a unit-test I want to test each rule, which can usually be done by calling the methods in the implementations. 
	
	@Test
	public void test3() {
		//let's say we start with a MessageReceived: 
		DqaMessageReceived mr = new DqaMessageReceived();
		DqaPatient p = new DqaPatient();
		p.setBirthDateString("2016-01-01");
		mr.setPatient(p);
		
		ValidationRule<DqaPatient> vr = new PatientBirthDateIsValid();
		List<ValidationIssue> issues = vr.executeRule(p, mr).getIssues();
		
		assertEquals("should be zero issues", 0, issues.size());
		
		p.setBirthDateString("");
		issues = vr.executeRule(p, mr).getIssues();
		assertEquals("should be two issues", 2, issues.size());
		
	}
	
	@Test
	public void testPatientIsUnderageDependencies() {
		List<Class> passed = new ArrayList<Class>();
		ValidationRule pu = new PatientIsUnderage();
		
		assertEquals("should be two dependencies: ", 2, pu.getDependencies().length);
		
		boolean dependenciesMet = pu.dependenciesAreMet(passed);
		assertEquals("PatientIsUnderage should not pass until birth date is validated and patient exists ", false, dependenciesMet);
		
		passed.add(PatientExists.class);
		
		dependenciesMet = pu.dependenciesAreMet(passed);
		assertEquals("PatientIsUnderage should not pass yet. It still needs one more rule to pass.", false, dependenciesMet);
		
		passed.add(PatientBirthDateIsValid.class);
		dependenciesMet = pu.dependenciesAreMet(passed);
		assertEquals("PatientIsUnderage should pass!  All dependencies are present.", true, dependenciesMet);
	}
 
	@Test
	public void testDependency() {

		List<Class<? extends ValidationRule>> passed = new ArrayList<Class<? extends ValidationRule>>(); 
		
		ValidationRule vr = new PatientExists();
		boolean dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("PatientExists deps should be met", true, dependenciesMet);
		
		ValidationRule pu = new PatientIsUnderage();
		dependenciesMet = pu.dependenciesAreMet(passed);
		assertEquals("PatientIsUnderage should not run until birth date is validated. ", false, dependenciesMet);
		
		vr = new PatientBirthDateIsValid();
		dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("PatientBirthDateIsValid deps should not be met", false, dependenciesMet);
		
		passed.add(PatientExists.class);
		dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("PatientBirthDateIsValid deps should NOW be met", true, dependenciesMet);
			
		vr = new VaccinationAdminAfterBirthDate();
		dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("VaccinationAdminAfterBirthDate Deps should not be met!", false, dependenciesMet);
		
		passed.add(VaccinationAdminDateIsValid.class);
		
		dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("VaccinationAdminAfterBirthDate Deps should not be met 2!", false, dependenciesMet);
		
		passed.add(PatientBirthDateIsValid.class);
		
		dependenciesMet = vr.dependenciesAreMet(passed);
		
		assertEquals("VaccinationAdminAfterBirthDate Deps should be met!", true, dependenciesMet);
		
		dependenciesMet = pu.dependenciesAreMet(passed);
		
		assertEquals("PatientIsUnderage should be able to run now", true, dependenciesMet);
		
	}
	
	
	/**
	 * This is testing the validation runner class specifically.  
	 */
	@Test
	public void validatePatient() {
		
		List<ValidationRule> testRules = Arrays.asList(new ValidationRule[] {
				new PatientBirthDateIsValid(), 
				new PatientExists(),
				new PatientIsUnderage()	
		});
		
		DqaMessageReceived m = getFreshMessage();
		m.getPatient().setBirthDateString("2016-01-01");
		
		List<ValidationRulePair> rpList = util.buildRulePairs(testRules, m.getPatient(),  m);

		List<ValidationRuleResult> results = runner.processValidationRules(rpList, null);
		
		assertEquals("Should have about three rules that ran", 3, results.size());
		
		//let's make the birth date bad!
		m.getPatient().setBirthDateString("");
		results = runner.processValidationRules(rpList, null);
		assertEquals("Should have about two rules that ran", 2, results.size());

		List<Class> passed = util.getPassedFromResults(results);
		assertEquals("only one that passes though", 1, passed.size());
		
		assertEquals("passed should only contain PatientExists.class", PatientExists.class, passed.get(0));

		List<Class> failed = util.getFailedFromResults(results);
		assertEquals("only one that fails too", 1, failed.size());
		assertEquals("failure should be PatientBirthDateIsValid.class", PatientBirthDateIsValid.class, failed.get(0)); 
		
		
	}

	@Test 
	public void patientRules() {
		List<ValidationRule> patientRules = ValidationRuleEntityLists.PATIENT_RULES.getRules();
		List<ValidationIssue> issues  = new ArrayList<ValidationIssue>();
		
		DqaMessageReceived mr = getFreshMessage();
		
		mr.getPatient().setBirthDateString("2016-01-01");
		
		for (ValidationRule rule : patientRules) {
			try {
				List<ValidationIssue> ruleIissues  = rule.executeRule(mr.getPatient(), mr).getIssues();
				issues.addAll(ruleIissues);
				
			} catch (Exception e) {
				System.out.println("Woah... nasty.  " + e.getLocalizedMessage());
				assertTrue("oops.  exception in " + rule.getClass() + "  " + e.getLocalizedMessage(), false);
			}
		}	
		System.out.println("Issues.size(): " + issues.size());
		for (ValidationIssue issue : issues) {
			System.out.println("ISSUE: " + issue.getIssue());
		}
		assertEquals("should be some issues", 16, issues.size());
	}
	
	@Test
	public void vaccinationRules() throws Exception {
		DqaMessageReceived mr = getFreshMessage();
		List<ValidationRule> vv = ValidationRuleEntityLists.VACCINATION_RULES.getRules();
		for (ValidationRule<DqaVaccination> vr : vv) {
			for (DqaVaccination v : mr.getVaccinations()) {
				List<ValidationIssue> issues = vr.executeRule(v, mr).getIssues();
			}
		}
	}

	private DqaMessageReceived getFreshMessage() {
		DqaMessageReceived mr = new DqaMessageReceived();
		DqaPatient p = new DqaPatient();
		p.setNameFirst("Johnathan");
		p.setNameMiddle("JingleHeimer");
		p.setPhoneNumber("5175555454");
		p.setNameLast("Scmitt");
		p.setAliasFirst("John");
		p.setNameTypeCode("Superhero");
		p.setMotherMaidenName("Cobbler");
		Address a = p.getAddress();
		a.setCity("Lansing");
		a.setStreet("121 Main");
		a.setStateCode("MI");
		a.setZip("48910");
		a.setCountyParishCode("42");
		a.setCountryCode("US");
		p.setBirthMultipleIndicator("N");
		p.setBirthOrderNumber("1");
//		p.setBirthDateString("2016-01-01");
		mr.setPatient(p);
		return mr;
	}
	
	
	
	
}
