package org.immregistries.dqa.validator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.validator.model.codes.VaccineCpt;
import org.immregistries.dqa.validator.model.codes.VaccineCvx;
import org.immregistries.dqa.validator.model.hl7types.Id;
import org.immregistries.dqa.validator.model.hl7types.OrganizationName;

public class DqaVaccination {

  public static final String ACTION_CODE_ADD = "A";
  public static final String ACTION_CODE_DELETE = "D";
  public static final String ACTION_CODE_UPDATE = "U";

  public static final String COMPLETION_COMPLETED = "CP";
  public static final String COMPLETION_NOT_ADMINISTERED = "NA";

  public static final String COMPLETION_PARTIALLY_ADMINISTERED = "PA";
  public static final String COMPLETION_REFUSED = "RE";
  public static final String INFO_SOURCE_ADMIN = "00";
  public static final String INFO_SOURCE_HIST = "01";

  private String action = "";//new CodedEntity(CodesetType.VACCINATION_ACTION_CODE);

  private List<String> adminCodeList = new ArrayList<String>();
  private String visCvxCode = "";
  private String adminNdc = "";//new CodedEntity(CodesetType.VACCINATION_NDC_CODE);
  private String adminCpt = "";//new CodedEntity(CodesetType.VACCINATION_CPT_CODE);
  private String adminCvx = "";//new CodedEntity(CodesetType.VACCINATION_CVX_CODE);
//  private VaccineCvx vaccineCvx = null;
//  private VaccineCpt vaccineCpt = null;
//  private VaccineNdc vaccineNdc = null;
  
  private Date adminDate = null;
  private String adminDateString = "";
  private Date adminDateEnd = null;
  private String adminDateEndString;
  private String systemEntryDateString;
  private Date systemEntryDate = null;
  private boolean administered = false;
  private String amount = "";
  private String amountUnit = "";//new CodedEntity(CodesetType.ADMINISTRATION_UNIT);
  private String bodyRoute = "";//new CodedEntity(CodesetType.BODY_ROUTE);
  private String bodySite = "";//new CodedEntity(CodesetType.BODY_SITE);
  private String completion = "";//new CodedEntity(CodesetType.VACCINATION_COMPLETION);
  private String confidentiality = "";//new CodedEntity(CodesetType.VACCINATION_CONFIDENTIALITY);
  private Id enteredBy = new Id(CodesetType.PHYSICIAN_NUMBER);
  private Date expirationDate = null;
  private String expirationDateString;
  private OrganizationName facility = new OrganizationName();
  private String facilityType = "";//new CodedEntity(CodesetType.FACILITY_TYPE);
  private String financialEligibility = "";//new CodedEntity(CodesetType.FINANCIAL_STATUS_CODE);
  private Id givenBy = new Id(CodesetType.PHYSICIAN_NUMBER);
  private String idPlacer = "";
  private String idSubmitter = "";
  private String informationSource = "";//new CodedEntity(CodesetType.VACCINATION_INFORMATION_SOURCE);
  private String lotNumber = "";
  private String manufacturer = "";//new CodedEntity(CodesetType.VACCINATION_MANUFACTURER_CODE);
  private DqaMessageReceived messageReceived = null;
  private List<Observation> observations = new ArrayList<Observation>();
  private String orderControl = "";//new CodedEntity(CodesetType.VACCINATION_ORDER_CONTROL_CODE);
  private Id orderedBy = new Id(CodesetType.PHYSICIAN_NUMBER);
  private int positionId;
  private int positionSubId;
  private String product = "";//new CodedEntity(CodesetType.VACCINE_PRODUCT);
  private String refusal = "";//new CodedEntity(CodesetType.VACCINATION_REFUSAL);
  private boolean skipped = false;
  private long vaccinationId = 0l;
  private String fundingSource = "";//new CodedEntity(CodesetType.VACCINATION_FUNDING_SOURCE);
  private String refusalReason = "";
  private List<VaccinationVIS> vaccinationVisList = new ArrayList<VaccinationVIS>();
  private String tradeName = "";//new CodedEntity(CodesetType.VACCINATION_TRADE_NAME);
  private String vaccineValidity = "";//new CodedEntity(CodesetType.VACCINATION_VALIDITY);

  public String getFacilityType()
  {
    return facilityType;
  }

  public String getFacilityTypeCode()
  {
    return facilityType;
  }

  public void setFacilityTypeCode(String facilityTypeCode)
  {
    this.facilityType = facilityTypeCode;
  }

  public String getTradeName()
  {
    return tradeName;
  }

  public String getTradeNameCode()
  {
    return tradeName;
  }

  public void setTradeNameCode(String tradeNameCode)
  {
    this.tradeName = tradeNameCode;
  }

  public String getVaccineValidity()
  {
    return vaccineValidity;
  }

  public String getVaccineValidityCode()
  {
    return vaccineValidity;
  }

  public void setVaccineValidityCode(String vaccineValidityCode)
  {
    this.vaccineValidity = vaccineValidityCode;
  }

  public List<VaccinationVIS> getVaccinationVisList()
  {
    return vaccinationVisList;
  }

  public String getRefusalReason()
  {
    return refusalReason;
  }

  public void setRefusalReason(String refusalReason)
  {
    this.refusalReason = refusalReason;
  }

  public String getFundingSourceCode()
  {
    return fundingSource;
  }

  public void setFundingSourceCode(String fundingSourceCode)
  {
    this.fundingSource = fundingSourceCode;
  }

  public Date getVisPresentedDate()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getPresentedDate() : null;
  }

  public void setVisPresentedDate(Date visPresentedDate)
  {
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setPresentedDate(visPresentedDate);
    }
  }

  public String getVisDocumentCode()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getDocumentCode() : "";
  }

  public void setVisDocumentCode(String visDocumentCode)
  {
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setDocumentCode(visDocumentCode);
    }
  }

  public String getFundingSource()
  {
    return fundingSource;
  }

  public String getAction()
  {
    return action;
  }

  public String getActionCode()
  {
    return action;
  }

  public String getAdminCptCode()
  {
    return adminCpt;
  }

  public String getAdminCvxCode()
  {
    return adminCvx;
  }

  // public static final String ACTION_CODE_ADD = "A";
  // public static final String ACTION_CODE_DELETE = "D";
  // public static final String ACTION_CODE_UPDATE = "U";
  //
  // public static final String INFO_SOURCE_ADMIN = "00";
  // public static final String INFO_SOURCE_HIST = "01";

  public Date getAdminDate()
  {
    return adminDate;
  }

  public Date getAdminDateEnd()
  {
    return adminDateEnd;
  }

  public String getAmount()
  {
    return amount;
  }

  public String getAmountUnit()
  {
    return amountUnit;
  }

  public String getAmountUnitCode()
  {
    return amountUnit;
  }

  public String getBodyRoute()
  {
    return this.bodyRoute;
  }

  public String getBodyRouteCode()
  {
    return this.bodyRoute;
  }

  public String getBodySite()
  {
    return this.bodySite;
  }

  public String getBodySiteCode()
  {
    return this.bodySite;
  }

  public String getCompletion()
  {
    return completion;
  }

  public String getCompletionCode()
  {
    return completion;
  }

  public String getConfidentialityCode()
  {
    return confidentiality;
  }

  public Id getEnteredBy()
  {
    return enteredBy;
  }

  public String getEnteredByNameFirst()
  {
    return enteredBy.getName().getFirst();
  }

  public String getEnteredByNameLast()
  {
    return enteredBy.getName().getLast();
  }

  public String getEnteredByNumber()
  {
    return enteredBy.getNumber();
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }

  public OrganizationName getFacility()
  {
    return facility;
  }
  
  public void setFacility(OrganizationName on)
  {
    this.facility = on;
  }


  public String getFacilityIdNumber()
  {
    return facility.getIdNumber();
  }

  public String getFacilityName()
  {
    return facility.getName();
  }

  public String getFinancialEligibilityCode()
  {
    return financialEligibility;
  }

  public Id getGivenBy()
  {
    return givenBy;
  }

  public String getGivenByNameFirst()
  {
    return givenBy.getName().getFirst();
  }

  public String getGivenByNameLast()
  {
    return givenBy.getName().getLast();
  }

  public String getGivenByNumber()
  {
    return givenBy.getNumber();
  }

  public String getIdPlacer()
  {
    return idPlacer;
  }

  public String getIdSubmitter()
  {
    return idSubmitter;
  }

  public String getInformationSource()
  {
    return informationSource;
  }
  
  public void setInformationSource(String ce)
  {
    this.informationSource = ce;
  }

  public String getInformationSourceCode()
  {
    return informationSource;
  }

  public String getLotNumber()
  {
    return lotNumber;
  }

  public String getManufacturer()
  {
    return manufacturer;
  }

  public String getManufacturerCode()
  {
    return manufacturer;
  }

  public DqaMessageReceived getMessageReceived()
  {
    return messageReceived;
  }

  public List<Observation> getObservations()
  {
    return observations;
  }

  public String getOrderControl()
  {
    return orderControl;
  }
  
  public void setOrderControl(String ce)
  {
	  this.orderControl = ce;
  }

  public String getOrderControlCode()
  {
    return orderControl;
  }

  public Id getOrderedBy()
  {
    return orderedBy;
  }

  public String getOrderedByNameFirst()
  {
    return orderedBy.getName().getFirst();
  }

  public String getOrderedByNameLast()
  {
    return orderedBy.getName().getLast();
  }

  public String getOrderedByNumber()
  {
    return orderedBy.getNumber();
  }

  public int getPositionId()
  {
    return positionId;
  }

  public String getProduct()
  {
    return this.product;
  }

  public String getRefusal()
  {
    return refusal;
  }

  public String getRefusalCode()
  {
    return refusal;
  }

  public Date getSystemEntryDate()
  {
    return systemEntryDate;
  }

  public long getVaccinationId()
  {
    return vaccinationId;
  }

//  public VaccineCvx getVaccineCvx()
//  {
//    return vaccineCvx;
//  }

  public Date getVisPublicationDate()
  {
    return vaccinationVisList.size() > 0 ? vaccinationVisList.get(0).getPublishedDate() : null;
  }

  public boolean isActionAdd()
  {
    return ACTION_CODE_ADD.equals(action);
  }

  public boolean isActionDelete()
  {
    return ACTION_CODE_DELETE.equals(action);
  }

  public boolean isActionUpdate()
  {
    return ACTION_CODE_UPDATE.equals(action);
  }

  public boolean isAdministered()
  {
    return administered;
  }

  public boolean isCompletionCompleted()
  {
    return COMPLETION_COMPLETED.equals(completion);
  }

  public boolean isCompletionNotAdministered()
  {
    return COMPLETION_NOT_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionPartiallyAdministered()
  {
    return COMPLETION_PARTIALLY_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionCompletedOrPartiallyAdministered()
  {
    return COMPLETION_COMPLETED.equals(completion) || COMPLETION_PARTIALLY_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionRefused()
  {
    return COMPLETION_REFUSED.equals(completion);
  }

  public boolean isInformationSourceAdmin()
  {
    return INFO_SOURCE_ADMIN.equals(informationSource);
  }

  public boolean isInformationSourceHist()
  {
    return INFO_SOURCE_HIST.equals(informationSource);
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setActionCode(String actionCode)
  {
    this.action = actionCode;
  }

  public void setAdminCptCode(String adminCptCode)
  {
    this.adminCpt = adminCptCode;
  }
  
  public void setAdminNdcCode(String code)
  {
    this.adminNdc = code;
  }


  public void setAdminCvxCode(String adminCvxCode)
  {
    this.adminCvx = adminCvxCode;
  }

  public void setAdminDate(Date adminDate)
  {
    this.adminDate = adminDate;
  }

  public void setAdminDateEnd(Date adminDateEnd)
  {
    this.adminDateEnd = adminDateEnd;
  }

  public void setAdministered(boolean administered)
  {
    this.administered = administered;
  }

  public void setAmount(String amount)
  {
    this.amount = amount;
  }

  public void setAmountUnitCode(String amountUnitCode)
  {
    amountUnit = amountUnitCode;
  }

  public void setBodyRouteCode(String bodyRouteCode)
  {
    this.bodyRoute = bodyRouteCode;
  }

  public void setBodySiteCode(String bodySiteCode)
  {
    this.bodySite = bodySiteCode;
  }

  public void setCompletionCode(String completionCode)
  {
    this.completion = completionCode;
  }

  public void setConfidentialityCode(String confidentialityCode)
  {
    confidentiality = confidentialityCode;
  }

  public void setEnteredByNameFirst(String enteredByNameFirst)
  {
    enteredBy.getName().setFirst(enteredByNameFirst);
  }

  public void setEnteredByNameLast(String enteredByNameLast)
  {
    enteredBy.getName().setLast(enteredByNameLast);
  }

  public void setEnteredByNumber(String enteredByNumber)
  {
    enteredBy.setNumber(enteredByNumber);
  }

  public void setExpirationDate(Date expirationDate)
  {
    this.expirationDate = expirationDate;
  }

  public void setFacilityIdNumber(String facilityIdNumber)
  {
    facility.setId(facilityIdNumber);
  }

  public void setFacilityName(String facilityName)
  {
    facility.setName(facilityName);
  }

  public void setFinancialEligibilityCode(String financialEligibilityCode)
  {
    this.financialEligibility = financialEligibilityCode;
  }

  public void setGivenByNameFirst(String givenByNameFirst)
  {
    givenBy.getName().setFirst(givenByNameFirst);
  }

  public void setGivenByNameLast(String givenByNameLast)
  {
    givenBy.getName().setLast(givenByNameLast);
  }

  public void setGivenByNumber(String givenByNumber)
  {
    givenBy.setNumber(givenByNumber);
  }

  public void setIdPlacer(String idPlacer)
  {
    this.idPlacer = idPlacer;
  }

  public void setIdSubmitter(String idSubmitter)
  {
    this.idSubmitter = idSubmitter;
  }

  public void setInformationSourceCode(String informationSourceCode)
  {
    informationSource = informationSourceCode;
  }

  public void setLotNumber(String lotNumber)
  {
    this.lotNumber = lotNumber;
  }

  public void setManufacturerCode(String manufacturerCode)
  {
    manufacturer = manufacturerCode;
  }

  public void setMessageReceived(DqaMessageReceived messageReceived)
  {
    this.messageReceived = messageReceived;
  }

  public void setObservations(List<Observation> observations)
  {
    this.observations = observations;
  }

  public void setOrderControlCode(String code)
  {
    orderControl = code;
  }

  public void setOrderedByNameFirst(String orderedByNameFirst)
  {
    orderedBy.getName().setFirst(orderedByNameFirst);
  }

  public void setOrderedByNameLast(String orderedByNameLast)
  {
    orderedBy.getName().setLast(orderedByNameLast);
  }

  public void setOrderedByNumber(String orderedByNumber)
  {
    orderedBy.setNumber(orderedByNumber);
  }

  public void setPositionId(int positionId)
  {
    this.positionId = positionId;
  }

  public void setRefusalCode(String refusalCode)
  {
    refusal = refusalCode;
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

  public void setSystemEntryDate(Date systemEntryDate)
  {
    this.systemEntryDate = systemEntryDate;
  }

  public void setVaccinationId(long vaccinationId)
  {
    this.vaccinationId = vaccinationId;
  }

//  public void setVaccineCvx(VaccineCvx vaccineCvx)
//  {
//    this.vaccineCvx = vaccineCvx;
//  }

  public void setVisPublicationDate(Date visPublicationDate)
  {
    if (vaccinationVisList.size() > 0)
    {
      vaccinationVisList.get(0).setPublishedDate(visPublicationDate);
    }
  }

public String getAdminDateString() {
	return adminDateString;
}

public void setAdminDateString(String adminDateString) {
	this.adminDateString = adminDateString;
}

/**
 * @return the adminNdc
 */
public String getAdminNdc() {
	return adminNdc;
}

///**
// * @return the vaccineCpt
// */
//public VaccineCpt getVaccineCpt() {
//	return vaccineCpt;
//}
//
///**
// * @param vaccineCpt the vaccineCpt to set
// */
//public void setVaccineCpt(VaccineCpt vaccineCpt) {
//	this.vaccineCpt = vaccineCpt;
//}

/**
 * @return the adminCodeList
 */
public List<String> getAdminCodeList() {
	return adminCodeList;
}

/**
 * @param adminCodeList the adminCodeList to set
 */
public void setAdminCodeList(List<String> adminCodeList) {
	this.adminCodeList = adminCodeList;
}

/**
 * @return the expirationDateString
 */
public String getExpirationDateString() {
	return expirationDateString;
}

/**
 * @param expirationDateString the expirationDateString to set
 */
public void setExpirationDateString(String expirationDateString) {
	this.expirationDateString = expirationDateString;
}

/**
 * @return the systemEntryDateString
 */
public String getSystemEntryDateString() {
	return systemEntryDateString;
}

/**
 * @param systemEntryDateString the systemEntryDateString to set
 */
public void setSystemEntryDateString(String systemEntryDateString) {
	this.systemEntryDateString = systemEntryDateString;
}

/**
 * @return the adminDateEndString
 */
public String getAdminDateEndString() {
	return adminDateEndString;
}

/**
 * @param adminDateEndString the adminDateEndString to set
 */
public void setAdminDateEndString(String adminDateEndString) {
	this.adminDateEndString = adminDateEndString;
}

/**
 * @return the positionSubId
 */
public int getPositionSubId() {
	return positionSubId;
}

/**
 * @param positionSubId the positionSubId to set
 */
public void setPositionSubId(int positionSubId) {
	this.positionSubId = positionSubId;
}

//public VaccineNdc getVaccineNdc() {
//	return vaccineNdc;
//}
//
//public void setVaccineNdc(VaccineNdc vaccineNdc) {
//	this.vaccineNdc = vaccineNdc;
//}


}
