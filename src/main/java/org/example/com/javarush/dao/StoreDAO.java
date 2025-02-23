package org.example.com.javarush.dao;

import org.example.com.javarush.domain.Store;
import org.hibernate.SessionFactory;

public class StoreDAO extends GenericDAO<Store> {

    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }
}
