package org.example.com.javarush.dao;

import org.example.com.javarush.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CityDAO extends GenericDAO<City> {
    public CityDAO(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }

    public City getByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<City> query = session.createQuery("FROM City WHERE city = :name", City.class);
        query.setParameter("name", name);
        return query.getSingleResult(); // Получаем результат запроса
    }
}
