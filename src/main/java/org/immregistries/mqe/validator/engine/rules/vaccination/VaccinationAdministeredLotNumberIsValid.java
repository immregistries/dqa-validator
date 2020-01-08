package org.immregistries.mqe.validator.engine.rules.vaccination;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.codebase.client.generated.Code;
import org.immregistries.codebase.client.generated.LinkTo;
import org.immregistries.codebase.client.reference.CodesetType;
import org.immregistries.mqe.validator.detection.Detection;
import org.immregistries.mqe.validator.detection.ImplementationDetail;
import org.immregistries.mqe.validator.detection.ValidationReport;
import org.immregistries.mqe.validator.engine.ValidationRule;
import org.immregistries.mqe.validator.engine.ValidationRuleResult;
import org.immregistries.mqe.validator.engine.codes.LotNumberInvalidFixes;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;

public class VaccinationAdministeredLotNumberIsValid extends ValidationRule<MqeVaccination> {

  @Override
  protected final Class[] getDependencies() {
    return new Class[] {VaccinationAdministeredLotNumberIsPresent.class};
  }

  public VaccinationAdministeredLotNumberIsValid() {
    {
      ImplementationDetail id = this.addRuleDetection(Detection.VaccinationLotNumberIsInvalid);
      id.setImplementationDescription(
          "Vaccination lot number must be comprised of alphanumeric characters and/or the '-'. All other characters are invalid.");
      id.setHowToFix("The vaccination lot number contains characters that are not expected in a regular lot number. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no additional information being included with the lot number. The manufacturer lot number field should only include the lot number and nothing else. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. Incorrectly recorded lot numbers may cause problems in lot decrementing and may not be recognized during lot recalls. ");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.VaccinationLotNumberFormatIsUnrecognized);
      id.setImplementationDescription(
          "Vaccination lot number doesn't match the expected format specified by the manufacturer code.");
      id.setHowToFix("The vaccination lot number does not look like the lot number that this manufacturer would assign. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no additional information being included with the lot number. The manufacturer lot number field should only include the lot number and nothing else. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. ");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.VaccinationLotNumberHasInvalidInfixes);
      id.setImplementationDescription("Vaccination lot number cannot contain the text ICE3");
      id.setHowToFix("The vaccination lot number contains additional characters that are not expected in a regular lot number. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no additional information being included with the lot number. The manufacturer lot number field should only include the lot number and nothing else.");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. Incorrectly recorded lot numbers may cause problems in lot decrementing and may not be recognized during lot recalls.");
    }
    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.VaccinationLotNumberHasInvalidPrefixes);
      id.setImplementationDescription(
          "Vaccination lot number cannot start with LOT, (P), MED, SKB, LOT, PMC, WSD, WAL");
      id.setHowToFix("The vaccination lot number starts with characters that are not expected as part of the start of any valid lot number. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no additional information being included with the lot number. The manufacturer lot number field should only include the lot number and nothing else. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. ");
    }

    {
      ImplementationDetail id =
          this.addRuleDetection(Detection.VaccinationLotNumberHasInvalidSuffixes);
      id.setImplementationDescription(
          "Vaccination lot number cannot end with (P), -P, -S, -C, -H, -V, *, #, (S), (P),\r\n"
              + "        MSD, HSP, SELECT, CP, VFC, STATE, CHIP, ADULT, ST-, PRIVATE, PED,\r\n"
              + "        UNINSURED, SPECIAL, OVER19, VMC, -COUNT, REAR, PENT, PENTACEL, DTAP,\r\n"
              + "        IPV, ACTH, HIB, PFF, FLU, BOOST, HAV, GARDASIL, ROTATEQ, PEDVAX,\r\n"
              + "        VARIVAX, PNEU, PNEUMOVAX, MMR, MENVEO, MENACTRA, FLU ZONE");
      id.setHowToFix("The vaccination lot number ends with characters that are not expected at the end of any valid lot number. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no additional information being included with the lot number. The manufacturer lot number field should only include the lot number and nothing else. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. ");
    }
    {
      ImplementationDetail id = this.addRuleDetection(Detection.VaccinationLotNumberHasMultiple);
      id.setImplementationDescription("Vaccination lot number has multiple lot numbers.");
      id.setHowToFix("The vaccination lot number field appears to contain more than one lot number. Please review the lot number recorded and ensure that only one lot number, the one that belongs to the active component of the vaccination, is being recorded. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. ");
    }
    {
      ImplementationDetail id = this.addRuleDetection(Detection.VaccinationLotNumberIsTooShort);
      id.setImplementationDescription("Vaccination lot number is 4 characters or less.");
      id.setHowToFix("The vaccination lot number is too short and therefore may not be a valid lot number. Please review the lot number you are sending and ensure that it contains the lot number exactly as it appears on the vaccination packaging and that there is no missing information from the correct lot number. ");
      id.setWhyToFix("The Lot Number is used for several critical IIS functions including: Vaccine lot inventory decrementing, vaccination matching, and vaccination product recall. Reporting the vaccine lot number helps to create a complete and accurate vaccination history. ");
    }
  }

  @Override
  protected ValidationRuleResult executeRule(MqeVaccination target, MqeMessageReceived m) {

    List<ValidationReport> issues = new ArrayList<ValidationReport>();
    boolean passed = true;

    if (this.common.isEmpty(target.getLotNumber())) {
      return buildResults(issues, false);
    }

    List<String> lotNumberList = builtLotNumberList(target, issues);

    for (String lotNumber : lotNumberList) {

      for (String prefix : LotNumberInvalidFixes.INSTANCE.getInvalidPrefixes()) {
        if (lotNumber.startsWith(prefix)) {
          issues.add(Detection.VaccinationLotNumberHasInvalidPrefixes.build(lotNumber, target));
          break;
        }
      }
      for (String infix : LotNumberInvalidFixes.INSTANCE.getInvalidInfixes()) {
        int pos = lotNumber.indexOf(infix);
        if (pos > 0 && pos < (lotNumber.length() - 1)) {
          issues.add(Detection.VaccinationLotNumberHasInvalidInfixes.build(lotNumber, target));
          break;
        }
      }
      for (String suffix : LotNumberInvalidFixes.INSTANCE.getInvalidSuffixes()) {
        if (lotNumber.endsWith(suffix)) {
          issues.add(Detection.VaccinationLotNumberHasInvalidSuffixes.build(lotNumber, target));
          break;
        }
      }
      for (char c : lotNumber.toCharArray()) {
        if (c >= '0' && c <= '9') {
          continue;
        }
        if (c == '-') {
          continue;
        }
        if (c >= 'a' && c <= 'z') {
          continue;
        }
        if (c >= 'A' && c <= 'Z') {
          continue;
        }
        issues.add(Detection.VaccinationLotNumberIsInvalid.build(lotNumber, target));
        break;
      }
      if (!StringUtils.isBlank(target.getManufacturerCode())) {
        Code vaccineMvx = repo.getMfrForCode(target.getManufacturerCode());
        if (vaccineMvx != null) {
          if (!validateLotNumber(target, issues, lotNumber, vaccineMvx)) {
            issues.add(Detection.VaccinationLotNumberFormatIsUnrecognized.build(lotNumber, target));
          }
        }
      }
    }

    passed = issues.isEmpty();

    return buildResults(issues, passed);
  }

  private List<String> builtLotNumberList(MqeVaccination target, List<ValidationReport> issues) {
    String lotNumbers = target.getLotNumber().toUpperCase();

    List<String> lotNumberList = new ArrayList<>();
    {
      boolean hasMultiple = false;
      List<String> separators = LotNumberInvalidFixes.INSTANCE.getInvalidSeparators();
      for (String sep : separators) {
        int pos = lotNumbers.indexOf(sep);
        while (pos > 0) {
          if (!hasMultiple) {
            issues.add(Detection.VaccinationLotNumberHasMultiple.build(lotNumbers, target));
            hasMultiple = true;
          }
          String lot1 = lotNumbers.substring(0, pos).trim();
          if (lot1.length() <= 4) {
            issues.add(Detection.VaccinationLotNumberIsTooShort.build(lot1, target));
          } else {
            lotNumberList.add(lot1);
          }
          lotNumbers = lotNumbers.substring(pos + sep.length()).trim();
          pos = lotNumbers.indexOf(sep);
        }
      }
      if (lotNumbers.length() <= 4) {
        issues.add(Detection.VaccinationLotNumberIsTooShort.build(lotNumbers, target));
      } else {
        lotNumberList.add(lotNumbers);
      }
    }
    return lotNumberList;
  }

  private boolean validateLotNumber(MqeVaccination target, List<ValidationReport> issues,
      String lotNumber, Code vaccineMvx) {

    if (vaccineMvx.getReference() != null && vaccineMvx.getReference().getLinkTo() != null) {
      boolean found = false;
      for (LinkTo linkTo : vaccineMvx.getReference().getLinkTo()) {
        if (linkTo.getCodeset().equals(CodesetType.VACCINATION_LOT_NUMBER_PATTERN.getType())) {
          String regex = linkTo.getValue();
          if (!StringUtils.isBlank(regex)) {
            found = true;
            try {
              Pattern pattern = Pattern.compile(regex);
              Matcher matcher = pattern.matcher(lotNumber);
              if (matcher.matches()) {
                return true;
              }
            } catch (PatternSyntaxException pse) {
              System.err.println("Unparsable Lot Number regular expression: " + regex);
            }
          }
        }
      }
      return !found;
    }
    return true;
  }
}
