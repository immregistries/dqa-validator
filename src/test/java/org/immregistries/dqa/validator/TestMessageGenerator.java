package org.immregistries.dqa.validator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.immregistries.dqa.vxu.DqaMessageReceived;

public class TestMessageGenerator {

  private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
  private static final String PID = "PID|||23456^^^^MR||LASTNAME^FIRSTNAME^LEGALMIDDLE^LEGALSUFFIX^^X~COOLNAME^AWESOMENAME2|MOMSMAIDEN|20100604|M||RACECD|100 Main^^Lansing^MI^48912-1330^US^^^60~^^^^^US^BDL^^123456||^PRN^PH^^^517^555-5555~(517)555-5555^P^H||en|||||||22|23||||||DEATHDT||||||\r";
  private static final String PID2 = "PID|||00000^^^^MR~111111^^^^WC~22222^^^^SS~33333^^^^MA~44444^^^^SR||LEGALLAST^^^^^X~COOLNAME^AWESOMENAME2||20120605|F|||100 Main^^Lansing^MI^48912-1330^US^~^^^^^US^BDL^^123456||(517)555-2222^P^H~^PRN^PH^^^517^555-5555||en||||\r";
  private static final String NK1 = "NK1||NKLast^NKFirst|MTH^3^HL70063|100 Main^^Lansing^MI^48912 1330^US^|5175555555|||||||||||||||20||||||||||30|||33^^^^SS||||37|\r";
  private static final String ORC = "ORC|||123456|\r";
  private static final String RXA = "RXA|0||20140614|20140614|83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXA2 = "RXA|0|X|20140615|20140615|87^Awesome Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXA3 = "RXA|0||20140616|20140616|89^IMPORTANT Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXR = "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r";
  private static final String OBX = "OBX|2|CE|64994-7^Vaccine funding program eligibility category^LN||V02^VFC Eligible - Medicaid/Medicare^HL70064||||||F|||20140614\r";

  private static String EXAMPLE_VXU;

  private static String EXAMPLE_VXU_II = MSH + PID2 + NK1 + ORC + RXA + RXR + OBX;

  private static String EXAMPLE_VXU_III =
      MSH + PID2 + NK1 + ORC + RXA + RXR + OBX + ORC + RXA + RXR + OBX;

  private final String CAIR_EXAMPLE =
            "MSH|^~\\&|1255-60-20|1255-60-20|CAIR|CAIRCC|20180424103726||VXU^V04^VXU_V04|Q103084826T103060205|P|2.5.1|||ER|AL||8859/1|||Z22^CDCPHINVS|WESTLANSING\r"
          + "PID|1||9x9x9x9x^^^MRN^MR~1x1x1x1x^^^MRN&2.16.840.1.000000.3.3540.100&ISO^MR||NCIT^TEST^^^^^L||19850904|F||1002-5^American Indian or Alaska Native^HL70005|375 HILLMONT AVE^^LANSING^MI^930031650^^H^^|||||A^Separated^HL70002||2004410958^^^FIN^AN||||2186-5^Non-Hispanic^CDCREC\r"
          + "PD1|||^^^^^^^^^WESTLANSING|||||||||N|20180424|||A|20180424\r"
          + "ORC|RE|774684925^SF-002634^2.16.840.1.113883.3.3540.42.999322.19271314^ISO|{71A3DE42-084E-4BCC-8B68-30AF1836DFB9}^SF-002634|||||||||^Chan^Tiffany^C^^PA|||||SF-002634^West Lansing Medical Clinic^HL70362\r"
          + "RXA|0|1|20180420134500||83^Hep A, ped/adol, 2 dose^CVX|0.5|mL^Milliliter^UCUM^^^^^^Milliliter||00^New immunization record^NIP001||^^^WESTLANSING^^^^^133 W Santa Clara Street^^Lansing^^93001||||987654321|20180420|SKB^Glaxo Smith Kline^MVX^^^^^^Glaxo Smith Kline|||CP|A|20180420134909\r"
          + "RXR|C28161^IM^NCIT^^^^^^IM|LD^Left Deltoid^HL70163^^^^^^Left Deltoid\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC Eligible^HL70064^^^^^^Not VFC Eligible||||||F|||20180420134909|||VXC40^Eligibility captured at the Immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|1|85^Hepatitis A^CVX^^^^^^Hepatitis A||||||F|||20180420134909\r"
          + "OBX|3|TS|29768-9^Date vaccine information sheet published^LN|1|20160720||||||F|||20180420134909";


  private static final Map<String, String> SEG_MAP = new LinkedHashMap<String, String>();
  private static final List<String> SEG_LIST = new LinkedList<String>();

  private static final String AIRA_TEST_MSG =
      "MSH|^~\\&|||||20160518151704-0400||VXU^V04^VXU_V04|5B6-B.10.05.1fa|P|2.5.1|\r"
          + "PID|||5B6-B.10.05^^^AIRA-TEST^MR||Latimer^Rocco^Janus^^^^L|Monona^Ushma|20120524|M||2106-3^White^HL70005|266 Lynch St^^Caspian^MI^49915^USA^P||^PRN^PH^^^906^3997846|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20160518|20160518|\r"
          + "NK1|1|Monona^Ushma^^^^^L|FTH^Father^HL70063|266 Lynch St^^Caspian^MI^49915^USA^P|^PRN^PH^^^906^3997846|\r"
          + "ORC|RE||E47Q115.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20160518||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||U1747GW||MSD^Merck and Co^MVX||||A|\r"
          + "RXR|SC^^HL70162|LA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20160518|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160518||||||F|\r";

  private static final String IMMUNITY_MSG =
      "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
          + "PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          + NK1
          + "ORC|RE||N54R81.2^AIRA|\r"
          + "RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
          + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
          + "ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";

  static {
    SEG_MAP.put("MSH.1", MSH);
    SEG_MAP.put("PID.1", PID);
    SEG_MAP.put("NK1.1", NK1);
    SEG_MAP.put("ORC.1", ORC);
    SEG_MAP.put("RXA.1", RXA);
    SEG_MAP.put("OBX.1", OBX);
    SEG_MAP.put("ORC.2", ORC);
    SEG_MAP.put("RXA.2", RXA2);
    SEG_MAP.put("OBX.2", OBX);
    SEG_MAP.put("RXR.1", RXR);
    SEG_MAP.put("RXA.3", RXA3);
    SEG_MAP.put("OBX.3", OBX);

    StringBuffer sb = new StringBuffer();
    for (String seg : SEG_MAP.values()) {
      sb.append(seg);
    }

    for (String segName : SEG_MAP.keySet()) {
      SEG_LIST.add(segName.substring(0, 3));
    }

    EXAMPLE_VXU = sb.toString();

  }

  public String getExampleVXU_1() {
    return EXAMPLE_VXU;
  }

  public String getExampleVXU_2() {
    return EXAMPLE_VXU_II;
  }

  public String getExampleVXU_3() {
    return EXAMPLE_VXU_III;
  }

  public String getImmunityMessage() {
    return IMMUNITY_MSG;
  }

  public Map<String, String> getSegmentMap1() {
    return SEG_MAP;
  }

  public String getAiraTestMsg() {
    return AIRA_TEST_MSG;
  }

  private DqaMessageService service = DqaMessageService.INSTANCE;

  public DqaMessageReceived getMsg1() {
    return service.extractMessageFromText(this.getExampleVXU_1());
  }

  public DqaMessageReceived getMsg2() {
    return service.extractMessageFromText(this.getExampleVXU_2());
  }

  public DqaMessageReceived getMsg3() {
    return service.extractMessageFromText(this.getImmunityMessage());
  }

  public DqaMessageReceived getMsg4() {
    return service.extractMessageFromText(this.getAiraTestMsg());
  }


  public String getCairExample() {
    return CAIR_EXAMPLE;
  }
}
