/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.manager.api.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.jpa.HibernatePersistenceProvider;

/**
 * Produces an instance of {@link EntityManagerFactory}. Or the managed persistence unit, if there is one :-).
 *
 * @author eric.wittmann@redhat.com
 */
@ApplicationScoped
public class EntityManagerFactoryAccessor implements IEntityManagerFactoryAccessor {

    @Inject
    private IJpaProperties jpaProperties;

    private EntityManagerFactory emf;

    // @PersistenceContext(unitName = "apiman-manager-api-jpa")
    // private EntityManager pcEm;

    private final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    /**
     * Constructor.
     */
    public EntityManagerFactoryAccessor() {
    }

    @PostConstruct
    public void postConstruct() {
        Map<String, String> properties = new HashMap<>();

        // Get properties from apiman.properties
        Map<String, String> cp = jpaProperties.getAllHibernateProperties();
        if (cp != null) {
            properties.putAll(cp);
        }

        // Get two specific properties from the System (for backward compatibility only)
        String s = properties.get("hibernate.hbm2ddl.auto");
        if (s == null) {
            s = "validate";
        }
        String autoValue = System.getProperty("apiman.hibernate.hbm2ddl.auto", s);
        s = properties.get("hibernate.dialect");
        if (s == null) {
            s = "org.hibernate.dialect.H2Dialect";
        }
        String dialect = System.getProperty("apiman.hibernate.dialect", s);
        properties.put("hibernate.hbm2ddl.auto", autoValue);
        properties.put("hibernate.dialect", dialect);

        // First try using standard JPA to load the persistence unit.  If that fails, then
        // try using hibernate directly in a couple ways (depends on hibernate version and
        // platform we're running on).

        // if (pcEm != null) {
        //     return;
        // }

        try {
            emf = Persistence.createEntityManagerFactory("apiman-manager-api-jpa", properties);
        } catch (Throwable t1) {
            try {
                emf = new HibernatePersistenceProvider().createEntityManagerFactory("apiman-manager-api-jpa", properties);
            } catch (Throwable t3) {
                throw t1;
            }
        }
    }

    /**
     * @see io.apiman.manager.api.jpa.IEntityManagerFactoryAccessor#getEntityManagerFactory()
     */
    @Override
    @Produces
    public EntityManagerFactory getEntityManagerFactory() {
        return getEntityManager().getEntityManagerFactory();
    }

    @Produces
    public EntityManager getEntityManager() {
        // if (pcEm != null) {
        //     return pcEm;
        // }
        EntityManager threadLocalEm = threadLocal.get();
        if (threadLocalEm != null && threadLocalEm.isOpen()) {
            return threadLocalEm;
        } else {
            EntityManager newEm = emf.createEntityManager();
            threadLocal.set(newEm);
            return newEm;
        }
    }

}