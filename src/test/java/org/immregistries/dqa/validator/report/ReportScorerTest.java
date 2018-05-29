package org.immregistries.dqa.validator.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.dqa.validator.DqaMessageService;
import org.immregistries.dqa.validator.DqaMessageServiceResponse;
import org.immregistries.dqa.validator.detection.DetectionType;
import org.immregistries.dqa.vxu.VxuField;
import org.immregistries.dqa.vxu.VxuObject;
import org.junit.Test;

public class ReportScorerTest {

  ReportScorer re = ReportScorer.INSTANCE;

  @Test
  public void testIssueScoring() {
    ReportIssue ri = generateReportIssueInvalid();
    int totalFieldCount = 100;
    int discoveredIssueCount = 10;
    double score = re.scoreIssuePercent(ri, discoveredIssueCount, totalFieldCount);
    assertEquals("1. expected .1", .10, score, 0);

    totalFieldCount = 1000;
    discoveredIssueCount = 100;
    score = re.scoreIssuePercent(ri, discoveredIssueCount, totalFieldCount);
    assertEquals("2. expected .1", .10, score, 0);

    ri.setMultiplierPercent(50);
    score = re.scoreIssuePercent(ri, discoveredIssueCount, totalFieldCount);
    assertEquals("expected .05", .05, score, 0);

  }

  public ReportIssue generateReportIssueInvalid() {
    ReportIssue ri = new ReportIssue();
    ri.setMultiplierPercent(100);
    ri.setType(DetectionType.INVALID);
    return ri;
  }

  public ReportIssue generateReportIssueMissing() {
    ReportIssue ri = new ReportIssue();
    ri.setMultiplierPercent(100);
    ri.setType(DetectionType.MISSING);
    return ri;
  }

  public ReportIssue generateReportIssueUnrecognized() {
    ReportIssue ri = new ReportIssue();
    ri.setMultiplierPercent(100);
    ri.setType(DetectionType.UNRECOGNIZED);
    return ri;
  }

  public List<ReportIssue> getDefaultIssueList() {
    List<ReportIssue> defaults = new ArrayList<>();
    defaults.add(generateReportIssueInvalid());
    defaults.add(generateReportIssueUnrecognized());
    defaults.add(generateReportIssueMissing());
    return defaults;
  }

  public DqaReportFieldDefinition generateReportFieldFirst() {
    DqaReportFieldDefinition def = new DqaReportFieldDefinition();
    def.setField(VxuField.PATIENT_NAME_FIRST);
    def.setWeight(100);
    def.getIssues().addAll(getDefaultIssueList());
    return def;
  }

  public DqaReportFieldDefinition generateReportFieldLast() {
    DqaReportFieldDefinition def = new DqaReportFieldDefinition();
    def.setField(VxuField.PATIENT_NAME_LAST);
    def.setWeight(100);
    def.getIssues().addAll(getDefaultIssueList());
    return def;
  }

  public DqaReportFieldDefinition generateReportFieldMiddle() {
    DqaReportFieldDefinition def = new DqaReportFieldDefinition();
    def.setField(VxuField.PATIENT_NAME_MIDDLE);
    def.setWeight(50);
    def.getIssues().addAll(getDefaultIssueList());
    return def;
  }

  public List<DqaReportFieldDefinition> getReportFieldList() {
    List<DqaReportFieldDefinition> fields = new ArrayList<>();
    fields.add(generateReportFieldFirst());
    fields.add(generateReportFieldLast());
    fields.add(generateReportFieldMiddle());
    return fields;
  }

  public ReportDefinition generateSingleFieldReport() {
    ReportDefinition r = new ReportDefinition();
    r.setLabel("First Name Report");
    ReportCompletenessSectionDefinition section = new ReportCompletenessSectionDefinition();
    section.setLabel("Completeness");
    section.setSectionObject(VxuObject.PATIENT);
    section.getReportFields().add(generateReportFieldFirst());
    r.getQualitySections().add(section);
    return r;
  }

  public ReportDefinition generatePatientNameReport() {
    ReportDefinition r = new ReportDefinition();
    r.setLabel("Patient Name Report");
    ReportCompletenessSectionDefinition section = new ReportCompletenessSectionDefinition();
    section.setLabel("Completeness");
    section.setSectionObject(VxuObject.PATIENT);
    section.getReportFields().addAll(getReportFieldList());
    r.getQualitySections().add(section);
    return r;
  }

  private DqaMessageService msgSrvc = DqaMessageService.INSTANCE;
  private MessageResponseEvaluator eval = MessageResponseEvaluator.INSTANCE;

  public DqaMessageMetrics getExampleMetrics() {
    DqaMessageServiceResponse response = msgSrvc.processMessage(this.testMessage);
    return eval.toMetrics(response);
  }

  @Test
  public void testWholeReport() {
    ReportDefinition def = generateSingleFieldReport();
    DqaMessageMetrics metrics = getExampleMetrics();
    VxuScoredReport scoredReport = re.getScoredReport(def, metrics);
    assertNotNull("Should have a non-null report!", scoredReport);
    assertEquals("should score pretty well", 100, scoredReport.getReportScore().getScored());

    //add more tests here...  need a 1000 point report!
    def = generatePatientNameReport();
    scoredReport = re.getScoredReport(def, metrics);
    assertNotNull("Should have a non-null report!", scoredReport);
    assertEquals("want this report to add up to a lot", 250,
        scoredReport.getReportScore().getScored());

    ReportDefinition defaultReport = ReportDefinitionBuilder.INSTANCE.getDeafult();
    VxuScoredReport scoredDefaultReport = re.getScoredReport(defaultReport, metrics);
    assertNotNull("Should have a non-null report!", scoredDefaultReport);
    System.out.println("default report: " + scoredDefaultReport);


  }

  @Test
  public void testScoreField() {
    DqaReportFieldDefinition def = generateReportFieldFirst();

    int totalFieldCount = 1000;
    int potentialScore = def.getWeight();
    //Perfect score...
    Map<DetectionType, Integer> issueCounts = new HashMap<>();

    int score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);
    assertEquals("Should be a 100 score", 100, score);

    //10% bad...
    issueCounts.put(DetectionType.INVALID, new Integer(100));
    score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);

    assertEquals("Should be a 90 score", 90, score);

    //100% bad...
    issueCounts.put(DetectionType.INVALID, new Integer(totalFieldCount));
    score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);

    assertEquals("Should be a 0 score", 0, score);

    //10% bad... twice
    totalFieldCount = 1000;
    issueCounts.put(DetectionType.INVALID, new Integer(100));
    issueCounts.put(DetectionType.UNRECOGNIZED, new Integer(100));
    score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);

    assertEquals("Should be a 80 score", 80, score);

    //10% bad... three times
    totalFieldCount = 1000;
    issueCounts.put(DetectionType.INVALID, new Integer(100));
    issueCounts.put(DetectionType.UNRECOGNIZED, new Integer(100));
    issueCounts.put(DetectionType.MISSING, new Integer(100));
    score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);

    assertEquals("Should be a 70 score", 70, score);

    //5% bad... three times
    //modify the issue factors:
    for (ReportIssue ri : def.getIssues()) {
      ri.setMultiplierPercent(50);
    }

    totalFieldCount = 1000;
    issueCounts.put(DetectionType.INVALID, new Integer(100));
    issueCounts.put(DetectionType.UNRECOGNIZED, new Integer(100));
    issueCounts.put(DetectionType.MISSING, new Integer(100));
    score = potentialScore - re
        .getIssueDemeritTotal(re.scoreIssues(def, issueCounts, totalFieldCount), 100);

    assertEquals("Should be a 85 score", 85, score);

  }


  @Test
  public void testRangedScore() {
    ReportIssue ri = generateReportIssueInvalid();
    ri.setLowerPercentBoundary(0);
    ri.setUpperPercentBoundary(50);
    int totalFieldCount = 100;
    int discoveredIssueCount = 10;
    //so here we're at 10% representation.
    //this will be a 20% reduction b/c
    //we're 20% up the scale of zero to fifty.
    double score = re.scoreIssuePercent(ri, discoveredIssueCount, totalFieldCount);
    assertEquals("1. expected .2", .20, score, 0);


  }

  @Test
  public void testRangedCalculator() {
    //The easy stuff.
    double result = re.calculateRangedScore(1, 0, 1);
    assertEquals("upper A", 1, result, 0);
    result = re.calculateRangedScore(1, 0, 0.5);
    assertEquals("upper B", 1, result, 0);
    result = re.calculateRangedScore(.6, 0, .5);
    assertEquals("upper B.2", 1, result, 0);
    result = re.calculateRangedScore(.4, 0, .5);
    assertEquals("test C.4", .8, result, 0);
    result = re.calculateRangedScore(.1, 0, .1);
    assertEquals("upper D", 1, result, 0);
    result = re.calculateRangedScore(.1, .3, .6);
    assertEquals("lower E", 0, result, 0);
    result = re.calculateRangedScore(.7, .3, .6);
    assertEquals("upper F", 1, result, 0);
    result = re.calculateRangedScore(.2, .3, .6);
    assertEquals("lower G", 0, result, 0);

    //For this next test series, the range is .3 to .7
    //that's a .4 range, which is easy to divide into
    //four.
    //.3  = 0
    //.4  = .25
    //.5  = .50
    //.6  = .75
    //.7  = 1
    result = re.calculateRangedScore(.35, .3, .7);
    assertEquals("ranged C.X", .125, result, 0.0001);
    result = re.calculateRangedScore(.6, .3, .7);
    assertEquals("ranged C.X", .75, result, 0.0001);
    result = re.calculateRangedScore(.65, .3, .7);
    assertEquals("ranged C.X", .875, result, 0.0001);
    result = re.calculateRangedScore(.7, .3, .7);
    assertEquals("ranged C.X", 1, result, 0.0001);

    result = re.calculateRangedScore(.1, 0, .5);
    assertEquals("ranged C", .2, result, 0);

    result = re.calculateRangedScore(.25, 0, .5);
    assertEquals("ranged C.3", .5, result, 0);
    result = re.calculateRangedScore(.4, 0, .5);
    assertEquals("ranged C.4", .8, result, 0);

    //

  }


  private String testMessage =
      "MSH|^~\\&|||||20170301131228-0500||VXU^V04^VXU_V04|3WzJ-A.01.01.2aF|P|2.5.1|" +
          "\nPID|||3WzJ-A.01.01^^^AIRA-TEST^MR||McCracken^Vinvella^^^^^L|Butler^Pauline|20130225|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|81 Page Pl^^GR^MI^49544^USA^P||^PRN^PH^^^616^9245843|||||||||2135-2^Hispanic or Latino^HL70005|"
          +
          "\nPD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20170301|20170301|" +
          "\nNK1|1|McCracken^Pauline^^^^^L|MTH^Mother^HL70063|81 Page Pl^^GR^MI^49544^USA^P|^PRN^PH^^^616^9245843|"
          +
          "\nORC|RE||V51L2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|"
          +
          "\nRXA|0|1|20170301||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y5841RR||MSD^Merck and Co^MVX||||A|"
          +
          "\nRXR|SC^^HL70162|RA^^HL70163|" +
          "\nOBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20170301|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|"
          +
          "\nOBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|" +
          "\nOBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|" +
          "\nOBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20170301||||||F|";

}
