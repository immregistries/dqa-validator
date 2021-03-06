package org.immregistries.mqe.validator.engine.rules.vaccination;

public class VaccinationMfrIsValidForAdministered extends VaccinationMfrIsValid {

    @Override
    protected final Class[] getDependencies() {
        return new Class[] { VaccinationSourceIsAdministered.class };
    }
}
