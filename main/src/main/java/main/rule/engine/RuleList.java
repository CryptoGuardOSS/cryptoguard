package main.rule.engine;

/**
 * The class containing the Rule Translations.
 * <p>STATUS: IC</p>
 *
 * @author franceme
 * @since 01.01
 */
public enum RuleList {
	//region Values
	BrokenCrypto(1, "Found broken crypto schemes"),
	BrokenHash(2, "Found broken hash functions"),
	ConstantKey(3, "Used constant keys in code"),
	UntrustedTrustMgr(4, "Uses untrusted TrustManager"),
	ExportGradePubKey(5, "Used export grade public Key"),
	UntrustedHostNameVer(6, "Used untrusted HostNameVerifier"),
	HTTP(7, "Used HTTP Protocol"),
	PBE(8, "Used < 1000 iteration for PBE"),
	ConstantSalt(9, "Found constant salts in code"),
	ConstantIV(10, "Found constant IV in code"),
	PredictableSeed(11, "Found predictable seeds in code"),
	CheckHostname(12, "Should check HostnameVerification manually"),
	UntrustedPRNG(13, "Used untrusted PRNG"),
	PredictableKeyStore(14, "Used Predictable KeyStore Password"),
	UNCREATEDRULE(-1, "Used as a placeholder as the default search value");
	//endregion

	//region Attributes
	private Integer ruleId;
	private String desc;
	//endregion

	//region Constructor
	RuleList(Integer ruleId, String desc) {
		this.ruleId = ruleId;
		this.desc = desc;
	}
	//endregion

	//region Getters

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
	 * <p>getRuleByRuleNumber.</p>
	 *
	 * @param ruleNumber a {@link java.lang.Integer} object.
	 * @return a {@link main.rule.engine.RuleList} object.
	 */
	public static RuleList getRuleByRuleNumber(Integer ruleNumber) {
		for (RuleList rule : RuleList.values())
			if (rule.getRuleId().equals(ruleNumber)) {
				return rule;
			}
		return RuleList.UNCREATEDRULE;
	}
	//endregion

}
