package org.example.com.javarush.dao;

import org.example.com.javarush.domain.Actor;
import org.hibernate.SessionFactory;

public class ActorDAO extends GenericDAO<Actor> {

    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }
}
