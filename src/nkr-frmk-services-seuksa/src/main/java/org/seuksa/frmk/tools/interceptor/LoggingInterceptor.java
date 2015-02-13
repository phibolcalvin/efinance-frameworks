package org.seuksa.frmk.tools.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Log log = LogFactory.getLog(methodInvocation.getMethod().getDeclaringClass());
        try {
            log.trace("Start " + methodInvocation.getMethod().getName());
            Object res = methodInvocation.proceed();
            log.trace("Fin " + methodInvocation.getMethod().getName());
            return res;
        }
        catch (Exception e) {
            log.error("Error in: " + methodInvocation.getMethod().getName() + " : " + e);
            throw e;
        }
    }

}
