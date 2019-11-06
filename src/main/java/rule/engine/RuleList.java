package rule.engine;

import CWE_Reader.CWE;
import CWE_Reader.CWEList;

import java.util.ArrayList;

/**
 * The class containing the Rule Translations.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @version 03.07.01
 * @since V01.00.01
 */
public enum RuleList {
    //region Values
    BrokenCrypto(1, new Integer[]{256}, "Found broken crypto schemes"),
    BrokenHash(2, new Integer[]{759}, "Found broken hash functions"),//CWE =?>  759, 760,
    ConstantKey(3, new Integer[]{547}, "Used constant keys in code"),
    UntrustedTrustMgr(4, new Integer[]{349}, "Uses untrusted TrustManager"),
    ExportGradePubKey(5, new Integer[]{321}, "Used export grade public Key"),
    UntrustedHostNameVer(6, new Integer[]{601}, "Used untrusted HostNameVerifier"),
    HTTP(7, new Integer[]{650}, "Used HTTP Protocol"),
    PBE(8, new Integer[]{326}, "Used < 1000 iteration for PBE"),//CWE =?> 916, 328
    ConstantSalt(9, new Integer[]{547}, "Found constant salts in code"),//CWE =?> 329
    ConstantIV(10, new Integer[]{547}, "Found constant IV in code"),
    PredictableSeed(11, new Integer[]{760}, "Found predictable seeds in code"),
    CheckHostname(12, new Integer[]{297}, "Should check HostnameVerification manually"),
    UntrustedPRNG(13, new Integer[]{338}, "Used untrusted PRNG"),
    PredictableKeyStore(14, new Integer[]{321}, "Used Predictable KeyStore Password"),//CWE =?> 259, 260, 261,521
    UNCREATEDRULE(-1, new Integer[]{-1}, "Used as a placeholder as the default search value");
    //endregion

    //region Attributes
    private Integer ruleId;
    private String desc;
    private Integer[] cweId;
    //endregion

    //region Constructor
    RuleList(Integer ruleId, Integer[] cweId, String desc) {
        this.ruleId = ruleId;
        this.desc = desc;
        this.cweId = cweId;
    }
    //endregion

    //region Getters

    /**
     * <p>getRuleByRuleNumber.</p>
     *
     * @param ruleNumber a {@link java.lang.Integer} object.
     * @return a {@link rule.engine.RuleList} object.
     */
    public static RuleList getRuleByRuleNumber(Integer ruleNumber) {
        for (RuleList rule : RuleList.values())
            if (rule.getRuleId().equals(ruleNumber)) {
                return rule;
            }
        return RuleList.UNCREATEDRULE;
    }

    /**
     * <p>Getter for the field <code>ruleId</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getRuleId() {
        return ruleId;
    }

    /**
     * <p>Getter for the field <code>desc</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDesc() {
        return desc;
    }
    //endregion

    //region Accessors

    /**
     * Getter for cweId
     *
     * <p>getCweId()</p>
     *
     * @return {@link Integer[]} - The cweId.
     */
    public Integer[] getCweId() {
        return cweId;
    }

    /**
     * <p>retrieveCWEInfo({@link CWE_Reader.CWEList})</p>
     *
     * @param list a {@link CWE_Reader.CWEList} object.
     * @return {@link java.util.ArrayList} object.
     */
    public ArrayList<CWE> retrieveCWEInfo(CWEList list) {
        ArrayList<CWE> out = new ArrayList<>();

        for (Integer cweId : this.cweId)
            out.add(list.CWE_Lookup(cweId));

        return out;
    }
    //endregion

}
