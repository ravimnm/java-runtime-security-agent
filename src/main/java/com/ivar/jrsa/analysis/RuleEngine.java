package com.ivar.jrsa.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RuleEngine {

    public static AnalysisResult analyze(
            String command) {

        int score = 0;

        List<String> reasons =
                new ArrayList<>();

        String cmd = command.toLowerCase();

        // Rule 1 : Encoded PowerShell

        if (Pattern.compile(
                "powershell.*(-enc|-encodedcommand)",
                Pattern.CASE_INSENSITIVE)
                .matcher(cmd)
                .find()) {

            score += 60;

            reasons.add(
                    "Encoded PowerShell detected");
        }

        // Rule 2 : Hidden window

        if (Pattern.compile(
                "-w\\s+hidden|-windowstyle\\s+hidden",
                Pattern.CASE_INSENSITIVE)
                .matcher(cmd)
                .find()) {

            score += 30;

            reasons.add(
                    "Hidden PowerShell window");
        }

        // Rule 3 : LOLBins

        if (cmd.contains("certutil")
                || cmd.contains("mshta")
                || cmd.contains("rundll32")
                || cmd.contains("regsvr32")) {

            score += 40;

            reasons.add(
                    "Living-off-the-land binary detected");
        }

        // Rule 4 : Command shell

        if (cmd.contains("cmd.exe")) {

            score += 20;

            reasons.add(
                    "Command shell execution");
        }

        // Rule 5 : Download utilities

        if (cmd.contains("curl")
                || cmd.contains("wget")) {

            score += 20;

            reasons.add(
                    "External download utility used");
        }

        RiskLevel riskLevel;
        Verdict verdict;

        if (score >= 80) {

            riskLevel = RiskLevel.CRITICAL;
            verdict = Verdict.MALICIOUS;

        } else if (score >= 50) {

            riskLevel = RiskLevel.HIGH;
            verdict = Verdict.HIGHLY_SUSPICIOUS;

        } else if (score >= 20) {

            riskLevel = RiskLevel.MEDIUM;
            verdict = Verdict.SUSPICIOUS;

        } else {

            riskLevel = RiskLevel.LOW;
            verdict = Verdict.BENIGN;
        }

        String recommendation =
                score >= 50
                        ? "Investigate immediately"
                        : "No immediate action required";

        return new AnalysisResult(
                score,
                riskLevel,
                verdict,
                reasons,
                recommendation);
    }
}