/**
 * Created on 13/02/2012
 */
package com.nokor.frmk.interceptor.audit;

import org.hibernate.Interceptor;

/**
 * TypeInterceptor
 * @author kong.phirun
 *
 */
public interface TypeInterceptor extends Interceptor {

    boolean supports(Object entity);

}
