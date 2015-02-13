package com.nokor.frmk.dao;


import java.lang.reflect.Proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nokor.frmk.annotation.QueryResourceInvocationHandler;
import com.nokor.frmk.security.dao.SecurityQueries;
import com.nokor.frmk.ui.menu.model.MenuQueries;

/**
 * 
 * @author prasnar
 *
 */
@Configuration
public class DomainQueriesContext {
    @Bean
    public QueryResourceInvocationHandler queryInvocationHandler() {
        return new QueryResourceInvocationHandler();
    }

    @Bean
    public MenuQueries menuQueries() {
        return getProxy(MenuQueries.class);
    }


    @Bean
    public SecurityQueries securityQueries() {
        return getProxy(SecurityQueries.class);
    }


    @Bean
    public SettingQueries settingQueries() {
        return getProxy(SettingQueries.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, queryInvocationHandler());
    }
}
