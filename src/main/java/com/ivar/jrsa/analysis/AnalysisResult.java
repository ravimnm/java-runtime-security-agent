package com.ivar.jrsa.analysis;

import java.util.List;

public record AnalysisResult(

        int riskScore,

        RiskLevel riskLevel,

        Verdict verdict,

        List<String> reasons,

        String recommendedAction

) {}