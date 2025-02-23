package org.example;

import org.example.com.javarush.dao.GenericDAO;
import org.example.com.javarush.domain.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class FilmDAO extends GenericDAO<Film> {
    public FilmDAO( SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }

    public Film getFirstAvailableFilmForRent() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select f from Film f " +
                "where f.id not in (select distinct film.id from Inventory )", Film.class);
        query.setMaxResults(1);
        return (Film) query.uniqueResult();
    }
}
