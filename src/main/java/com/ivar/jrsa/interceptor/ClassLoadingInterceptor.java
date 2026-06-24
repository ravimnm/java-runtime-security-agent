package com.ivar.jrsa.interceptor;

import java.util.concurrent.Callable;

import com.ivar.jrsa.event.EventSeverity;
import com.ivar.jrsa.event.SecurityEvent;
import com.ivar.jrsa.event.SecurityEventType;
import com.ivar.jrsa.sender.SmtapClient;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class ClassLoadingInterceptor {

    @RuntimeType
    public static Object intercept(
            @AllArguments Object[] args,
            @SuperCall Callable<?> zuper)
            throws Exception {

        String className = "UNKNOWN";

        if (args != null && args.length > 0) {

            className = String.valueOf(args[0]);
        }

        System.out.println(
                "\n[JRSA] Dynamic Class Loading Detected");

        System.out.println(
                "Class : " + className);

        SecurityEvent event =
                new SecurityEvent(
                        SecurityEventType.CLASS_LOADING,
                        EventSeverity.HIGH,
                        "JRSA",
                        "Class loaded dynamically : "
                                + className);

        SmtapClient.send(event);

        return zuper.call();
    }
}