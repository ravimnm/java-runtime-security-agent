package com.ivar.jrsa.interceptor;

import java.util.concurrent.Callable;

import com.ivar.jrsa.analysis.AnalysisResult;
import com.ivar.jrsa.analysis.RuleEngine;
import com.ivar.jrsa.event.EventSeverity;
import com.ivar.jrsa.event.SecurityEvent;
import com.ivar.jrsa.event.SecurityEventType;
import com.ivar.jrsa.policy.PolicyEngine;
import com.ivar.jrsa.sender.SmtapClient;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class ProcessExecutionInterceptor {

    @RuntimeType
    public static Object intercept(
            @AllArguments Object[] args,
            @SuperCall Callable<?> zuper)
            throws Exception {

        String command = "UNKNOWN";

        if (args != null && args.length > 0) {

            command = String.valueOf(args[0]);
        }

        System.out.println(
                "\n[JRSA] Process Execution Detected");

        System.out.println(
                "Command : " + command);

        AnalysisResult result =
                RuleEngine.analyze(command);

        System.out.println(
                "Risk Score : "
                        + result.riskScore());

        System.out.println(
                "Verdict : "
                        + result.verdict());

        result.reasons()
                .forEach(
                        reason ->
                                System.out.println(
                                        "- " + reason));

        SecurityEvent event =
                new SecurityEvent(
                        SecurityEventType.PROCESS_EXECUTION,
                        EventSeverity.valueOf(
                                result.riskLevel().name()),
                        "JRSA",
                        """
                        Command : %s
                        Risk Score : %d
                        Verdict : %s
                        Reasons : %s
                        """
                                .formatted(
                                        command,
                                        result.riskScore(),
                                        result.verdict(),
                                        result.reasons()));

        long start = System.nanoTime();

        SmtapClient.send(event);

        long end = System.nanoTime();

        System.out.println(
                "Latency = "
                + ((end - start) / 1_000_000.0)
                + " ms");

        if (PolicyEngine.shouldBlock(result)) {

            throw new SecurityException(
                    "Blocked by JRSA");
        }

        return zuper.call();
    }
}