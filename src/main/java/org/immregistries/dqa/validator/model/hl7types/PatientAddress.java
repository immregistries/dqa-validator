/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.immregistries.dqa.validator.model.hl7types;

import org.immregistries.dqa.validator.model.DqaPatient;

public class PatientAddress extends Address
{
  private long addressId = 0;
  private DqaPatient patient = null;
  private int positionId = 0;
  private boolean skipped = false;

  public PatientAddress() {
	  //default constructor. 
  }
  
  public PatientAddress(Address a) {
		this.setStreet(a.getStreet());
        this.setStreet2(a.getStreet2());
        this.setCity(a.getCity());
        this.setStateCode(a.getStateCode());
        this.setZip(a.getZip());
        this.setCountryCode(a.getCountryCode());
        this.setTypeCode(a.getTypeCode());
        this.setCountyParishCode(a.getCountyParishCode());
  }
  
  public long getAddressId()
  {
    return addressId;
  }

  public void setAddressId(long addressId)
  {
    this.addressId = addressId;
  }

  public int getPositionId()
  {
    return positionId;
  }

  public void setPositionId(int positionId)
  {
    this.positionId = positionId;
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

  public DqaPatient getPatient()
  {
    return patient;
  }

  public void setPatient(DqaPatient patient)
  {
    this.patient = patient;
  }
  
}
