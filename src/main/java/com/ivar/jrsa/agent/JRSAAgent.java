package com.ivar.jrsa.agent;
import static net.bytebuddy.matcher.ElementMatchers.*;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.none;

import com.ivar.jrsa.interceptor.ProcessExecutionInterceptor;
import com.ivar.jrsa.interceptor.ReflectionInterceptor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;

public class JRSAAgent {

    public static void premain(String args,
                               Instrumentation inst) {

        System.out.println("[JRSA] Agent started");

        new AgentBuilder.Default()

                .ignore(nameStartsWith("net.bytebuddy.")
                        .or(nameStartsWith("java."))
                        .or(nameStartsWith("jdk."))
                        .or(nameStartsWith("sun.")))

                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)

                .type(named("com.ivar.jrsa.agent.ReflectionDemo"))

                .transform((builder,
                             typeDescription,
                             classLoader,
                             module,
                             protectionDomain) ->

                        builder.method(named("dangerousReflection"))
                               .intercept(
                                       MethodDelegation.to(
                                    		   ReflectionInterceptor.class)))

                .installOn(inst);
    }
}