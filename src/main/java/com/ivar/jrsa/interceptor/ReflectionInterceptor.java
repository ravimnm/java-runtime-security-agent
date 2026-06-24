package com.ivar.jrsa.interceptor;

import java.util.concurrent.Callable;

import com.ivar.jrsa.event.EventSeverity;
import com.ivar.jrsa.event.SecurityEvent;
import com.ivar.jrsa.event.SecurityEventType;
import com.ivar.jrsa.sender.SmtapClient;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class ReflectionInterceptor {

    @RuntimeType
    public static Object intercept(
            @Origin String methodName,
            @SuperCall Callable<?> zuper)
            throws Exception {

        System.out.println(
                "\n[JRSA] Reflection Usage Detected");

        System.out.println(
                "Method : " + methodName);

        SecurityEvent event =
                new SecurityEvent(
                        SecurityEventType.REFLECTION_USAGE,
                        EventSeverity.MEDIUM,
                        "JRSA",
                        "Reflection invocation : "
                                + methodName);

        SmtapClient.send(event);

        return zuper.call();
    }
}