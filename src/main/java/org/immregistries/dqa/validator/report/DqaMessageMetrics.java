package org.immregistries.dqa.validator.report;

import java.util.HashMap;
import java.util.Map;
import org.immregistries.dqa.validator.detection.Detection;
import org.immregistries.dqa.vxu.VxuObject;
import org.immregistries.dqa.validator.report.codes.CodeCollection;
import org.immregistries.dqa.validator.report.codes.VaccineCollection;
import org.joda.time.DateTime;

public class DqaMessageMetrics {

  private Map<VxuObject, Integer> objectCounts = new HashMap<>();
  private Map<Detection, Integer> attributeCounts = new HashMap<>();
  private Map<Integer, Integer> patientAgeCounts = new HashMap<>();
  private CodeCollection codes = new CodeCollection();
  private VaccineCollection vaccinations = new VaccineCollection();
  private DateTime firstMessageReceived;
  private DateTime lastMessageReceived;

  public Map<Integer, Integer> getPatientAgeCounts() {
    return patientAgeCounts;
  }

  public void setPatientAgeCounts(Map<Integer, Integer> patientAgeCounts) {
    this.patientAgeCounts = patientAgeCounts;
  }

  public VaccineCollection getVaccinations() {
    return vaccinations;
  }

  public void setVaccinations(VaccineCollection vaccinations) {
    this.vaccinations = vaccinations;
  }

  /* Getters and setters: */
  public CodeCollection getCodes() {
    return codes;
  }

  public void setCodes(CodeCollection codes) {
    this.codes = codes;
  }

  public Map<VxuObject, Integer> getObjectCounts() {
    return objectCounts;
  }

  public void setObjectCounts(Map<VxuObject, Integer> objectCounts) {
    this.objectCounts = objectCounts;
  }

  public Map<Detection, Integer> getAttributeCounts() {
    return attributeCounts;
  }

  public void setAttributeCounts(Map<Detection, Integer> attributeCounts) {
    this.attributeCounts = attributeCounts;
  }

  public DateTime getFirstMessageReceived() {
    return firstMessageReceived;
  }

  public void setFirstMessageReceived(DateTime firstMessageReceived) {
    this.firstMessageReceived = firstMessageReceived;
  }

  public DateTime getLastMessageReceived() {
    return lastMessageReceived;
  }

  public void setLastMessageReceived(DateTime lastMessageReceived) {
    this.lastMessageReceived = lastMessageReceived;
  }

  @Override
  public String toString() {
    return "DqaMessageMetrics [objectCounts=" + objectCounts + ",patientAgeCounts="
        + patientAgeCounts + ", attributeCounts=" + attributeCounts + ", codeCounts=" + codes
        + ", firstMessageReceived=" + firstMessageReceived + ", lastMessageReceived="
        + lastMessageReceived + "]";
  }


}
