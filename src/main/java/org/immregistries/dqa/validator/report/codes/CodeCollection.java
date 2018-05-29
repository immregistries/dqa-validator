package org.immregistries.dqa.validator.report.codes;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.core.util.DateUtility;
import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.PatientImmunity;
import org.immregistries.dqa.vxu.VxuField;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeCollection {

  private static final Logger logger = LoggerFactory.getLogger(CodeCollection.class);

  @Override
  public String toString() {
    return "CodeCollection{ counts=" + codeCountList + '}';
  }

  /*
   * For each message, we'll collect codes sent through. we're not detecting anything about the
   * codes. Just counting. The codes need to be categorized by type... maybe. except each code
   * should have a type. and maybe the Code type should be from CodeBase
   */

  public List<CollectionBucket> getCodeCountList() {
    return codeCountList;
  }

  public void setCodeCountList(List<CollectionBucket> list) {
    this.codeCountList = list;
  }

  /**
   * A map of Types.
   */
  // Map<CollectionBucket, Map<String, Integer>> vaccineCodeCounts = new HashMap<>();
  List<CollectionBucket> codeCountList = new ArrayList<>();

  public CodeCollection combine(CodeCollection other) {
    CodeCollection crNew = new CodeCollection();
    // crNew.vaccineCodeCounts.putAll(mergeTwo(this.vaccineCodeCounts, other.vaccineCodeCounts));
    crNew.codeCountList.addAll(mergeTwo(this.codeCountList, other.codeCountList));
    return crNew;
  }

  public void add(CollectionBucket cbIn) {
    this.codeCountList.add(cbIn);
  }

  List<CollectionBucket> mergeTwo(List<CollectionBucket> one, List<CollectionBucket> two) {
    if (one.size() > 0 && two.size() > 0) {
      List<CollectionBucket> cbNew = new ArrayList<>();
      for (CollectionBucket cbOne : one) {
        int x = two.indexOf(cbOne);
        if (x > -1) {
          CollectionBucket cbTwo = two.get(x);
          int countNew = cbTwo.getCount() + cbOne.getCount();
          CollectionBucket cbClone =
              new CollectionBucket(cbOne.getType(), cbOne.getAttribute(), cbOne.getValue());
          cbClone.setCount(countNew);
          cbNew.add(cbClone);
        }
      }

      return cbNew;
    } else if (one.size() > 0 && two.size() < 1) {
      return one;
    } else {
      return two;
    }
  }

  public CodeCollection() {
    // default.
  }


  List<CollectionBucket> getByType(CodesetType desiredType) {
    List<CollectionBucket> cbNew = new ArrayList<>();
    for (CollectionBucket bucket : this.codeCountList) {
      if (desiredType.getType().equals(bucket.getType())) {
        cbNew.add(bucket);
      }
    }
    return cbNew;
  }

  public CodeCollection(DqaMessageReceived message) {
    this.codeCountList = collectMessageCodesNew(message);
  }

  private List<CollectionBucket> collectMessageCodesNew(DqaMessageReceived message) {
    List<CollectionBucket> bucketList = new ArrayList<>();

    // from the message, count the types of codes.
    DateTime birthDate =
        DateUtility.INSTANCE.parseDateTime(message.getPatient().getBirthDateString());
    List<DqaVaccination> vaccineList = message.getVaccinations();
    // merge vaccine list with message header list
    DqaMessageHeader dqaMessageHeader = message.getMessageHeader();
    // merge vaccine and message header list with next of kin list
    List<DqaNextOfKin> kinList = message.getNextOfKins();
    // merge vaccine, message header, and next of kin list with patient list
    DqaPatient patient = message.getPatient();

    for (DqaVaccination v : vaccineList) {
      // calculate the vaccine admin date - birth date to get a year.
      DateTime adminDate = DateUtility.INSTANCE.parseDateTime(v.getAdminDateString());
      Integer adminAge = DateUtility.INSTANCE.getYearsBetween(birthDate, adminDate);
      String adminAgeString = String.valueOf(adminAge);
      // find the admin type.
      String adminType = "Historical";

      if (v.isAdministered()) {
        adminType = "Administered";
        // for (String group : v.getVaccineGroupsDerived()) {
        // addCounts(CodesetType.VACCINE_GROUP, adminAgeString, group,
        // bucketList);
        // }
      }
      MetaFieldInfo mfi = v.getMetaFieldInfo(VxuField.VACCINATION_ADMIN_CODE);

      String loc = "";

      if (mfi != null) {
        Hl7Location l = mfi.getHl7Location();
        loc = l.getBaseReference();
      }

//      addCounts(CodesetType.VACCINATION_ACTION_CODE, adminType, v.getActionCode(), bucketList);
      addCounts(VxuField.VACCINATION_ACTION_CODE, loc, v.getActionCode(), bucketList);

      addCounts(VxuField.VACCINATION_CVX_CODE, v.getInformationSource(), v.getAdminCvxCode(), bucketList);
      addCounts(VxuField.VACCINATION_ADMINISTERED_UNIT, adminType, v.getAmountUnit(), bucketList);
      addCounts(VxuField.VACCINATION_BODY_ROUTE, adminType, v.getBodyRouteCode(), bucketList);
      addCounts(VxuField.VACCINATION_BODY_SITE, adminType, v.getBodySiteCode(), bucketList);
      addCounts(VxuField.VACCINATION_COMPLETION_STATUS, adminType, v.getCompletionCode(), bucketList);
      addCounts(VxuField.VACCINATION_CONFIDENTIALITY_CODE, adminType, v.getConfidentialityCode(),
          bucketList);
      addCounts(VxuField.VACCINATION_CVX_CODE, adminType, v.getAdminCvxCode(), bucketList);
      addCounts(VxuField.VACCINATION_CPT_CODE, adminType, v.getAdminCptCode(), bucketList);
      addCounts(VxuField.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, adminType, v.getFinancialEligibilityCode(),
          bucketList);
      addCounts(VxuField.VACCINATION_INFORMATION_SOURCE, "RXA-9", v.getInformationSource(),
          bucketList);
      addCounts(VxuField.VACCINATION_MANUFACTURER_CODE, adminType, v.getManufacturerCode(),
          bucketList);
      addCounts(VxuField.VACCINATION_PRODUCT, adminType, v.getProduct(), bucketList);
      addCounts(VxuField.VACCINATION_REFUSAL_REASON, adminType, v.getRefusalCode(), bucketList);
      addCounts(VxuField.VACCINATION_NDC_CODE, adminType, v.getAdminNdc(), bucketList);
    }

    addCounts(VxuField.MESSAGE_ACCEPT_ACK_TYPE, "", dqaMessageHeader.getAckTypeAcceptCode(),
        bucketList);
    addCounts(VxuField.MESSAGE_ACCEPT_ACK_TYPE, "", dqaMessageHeader.getAckTypeApplicationCode(),
        bucketList);

    for (DqaNextOfKin k : kinList) {
      if (k.getAddress() != null) {
        addCounts(VxuField.NEXT_OF_KIN_ADDRESS_TYPE, "", k.getAddress().getTypeCode(), bucketList);
      }
      addCounts(VxuField.NEXT_OF_KIN_RELATIONSHIP, "", k.getRelationshipCode(), bucketList);
    }

    if (patient.getPatientAddress() != null) {
      addCounts(VxuField.PATIENT_ADDRESS_TYPE, "", patient.getPatientAddress().getTypeCode(), bucketList);
    }

    addCounts(VxuField.PATIENT_BIRTH_ORDER, "", patient.getBirthOrder(), bucketList);
    addCounts(VxuField.PATIENT_CLASS, "", patient.getPatientClassCode(), bucketList);
    addCounts(VxuField.PATIENT_ETHNICITY, "", patient.getEthnicityCode(), bucketList);
    addCounts(VxuField.PATIENT_GENDER, "", patient.getSexCode(), bucketList);

    for (PatientImmunity p : patient.getPatientImmunityList()) {
      addCounts(VxuField.PATIENT_IMMUNITY_CODE, "", p.getImmunityCode(), bucketList);
    }

    addCounts(VxuField.PATIENT_NAME_TYPE_CODE, "", patient.getNameTypeCode(), bucketList);
    if (patient.getPhone() != null) {
      addCounts(VxuField.PATIENT_PHONE_TEL_USE_CODE, "", patient.getPhone().getTelUseCode(),
          bucketList);
      addCounts(VxuField.PATIENT_PHONE_TEL_EQUIP_CODE, "", patient.getPhone().getTelEquipCode(),
          bucketList);
    }
    addCounts(VxuField.PATIENT_PRIMARY_LANGUAGE, "", patient.getPrimaryLanguageCode(), bucketList);
    addCounts(VxuField.PATIENT_PRIMARY_PHYSICIAN_ID, "", patient.getPhysicianNumber(), bucketList);
    addCounts(VxuField.PATIENT_PROTECTION_INDICATOR, "", patient.getProtectionCode(), bucketList);
    addCounts(VxuField.PATIENT_PUBLICITY_CODE, "", patient.getPublicityCode(), bucketList);
    addCounts(VxuField.PATIENT_RACE, "", patient.getRaceCode(), bucketList);
    addCounts(VxuField.PATIENT_VFC_STATUS, "", patient.getFinancialEligibilityCode(),
        bucketList);

    return bucketList;
  }

  void addCounts(VxuField vf, String attribute, String value, List<CollectionBucket> existing) {
    if (StringUtils.isBlank(value)) {
      return;// don't add anything to the count.
    }
    CollectionBucket cbLookup = new CollectionBucket(vf, attribute, value);
    int idx = existing.indexOf(cbLookup);
    if (idx > -1) {
      CollectionBucket cb = existing.get(idx);
      cb.setCount(cb.getCount() + 1);
    } else {
      cbLookup.setCount(1);
      existing.add(cbLookup);
    }
  }
}
