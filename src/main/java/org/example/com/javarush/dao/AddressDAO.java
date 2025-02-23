package org.example.com.javarush.dao;

import org.example.com.javarush.domain.Address;
import org.hibernate.SessionFactory;

public class AddressDAO extends GenericDAO<Address> {

    public AddressDAO(SessionFactory sessionFactory) {
        super(Address.class, sessionFactory);
    }
}
