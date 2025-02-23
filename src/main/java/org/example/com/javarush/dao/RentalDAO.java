package org.example.com.javarush.dao;

import org.example.com.javarush.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class RentalDAO extends GenericDAO<Rental> {
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public Rental getUnreturnedRental() {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("select r from Rental r where r.returnDate is null ");
        query.setMaxResults(1);
        return (Rental) query.getSingleResult();
    }
}
