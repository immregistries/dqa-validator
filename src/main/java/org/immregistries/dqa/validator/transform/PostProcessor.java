package org.immregistries.dqa.validator.transform;

import java.util.List;

import org.immregistries.dqa.validator.engine.issues.ValidationIssue;

/**
 * The intention of this class is to operate on the list of Issues
 * that are produced from this process. 
 * 
 * This will handle things like the concept of a date cliff
 * where we don't really care about dates after a certain point, 
 * so we downgrade some of the issue levels for vaccines administered 
 * after that date.
 * 
 * This class will also handle custom issue levels at the IIS level, 
 * or at the profile level. 
 * 
 * TODO:  come up with some notation for specifying the level for an issue. 
 * @author Josh Hull
 *
 */
public class PostProcessor {

	public List<ValidationIssue> transformList(List<ValidationIssue> listIn) {
		return null;
	}
}
