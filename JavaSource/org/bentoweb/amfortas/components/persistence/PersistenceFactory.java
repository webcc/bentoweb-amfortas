package org.bentoweb.amfortas.components.persistence;

import org.apache.avalon.framework.component.Component;

public interface PersistenceFactory extends Component {
        String ROLE = PersistenceFactory.class.getName();

        public org.hibernate.Session createSession();
}