package com.ivar.jrsa.policy;

import com.ivar.jrsa.analysis.AnalysisResult;
import com.ivar.jrsa.analysis.RiskLevel;

public final class PolicyEngine {

    private PolicyEngine() {
    }

    // Configuration thresholds
    private static final RiskLevel ALERT_THRESHOLD =
            RiskLevel.HIGH;

    private static final RiskLevel BLOCK_THRESHOLD =
            RiskLevel.CRITICAL;

    private static final RiskLevel STORE_THRESHOLD =
            RiskLevel.MEDIUM;

    /**
     * Should the event be sent to SMTAP?
     */
    public static boolean shouldStore(
            AnalysisResult result) {

        return result.riskLevel().ordinal()
                >= STORE_THRESHOLD.ordinal();
    }

    /**
     * Should notifications be sent?
     */
    public static boolean shouldAlert(
            AnalysisResult result) {

        return result.riskLevel().ordinal()
                >= ALERT_THRESHOLD.ordinal();
    }

    /**
     * Should JRSA block the action?
     */
    public static boolean shouldBlock(
            AnalysisResult result) {

        return result.riskLevel().ordinal()
                >= BLOCK_THRESHOLD.ordinal();
    }
}