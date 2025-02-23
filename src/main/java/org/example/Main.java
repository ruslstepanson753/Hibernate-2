package org.example;


import org.example.com.javarush.dao.*;
import org.example.com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;


    public Main() {
        Properties prop = new Properties();
        prop.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        prop.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        prop.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        prop.put(Environment.USER, "root");
        prop.put(Environment.PASS, "root");
        prop.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        prop.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(prop)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Customer customer = main.createCustomer();
        main.customerReturnInventoryToStore();
        main.customerRentInventory(customer);
        main.newFilmWasMade();
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Store store = storeDAO.getItems(0, 1).get(0);
            City city = cityDAO.getByName("Saint Louis");

            Address address = new Address();
            address.setAddress("Korablestroiteley 5");
            address.setPhone("03");
            address.setCity(city);
            address.setDistrict("omg");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setAddress(address);
            customer.setActive(true);
            customer.setLastName("Ivanov");
            customer.setFirstName("Ivan");
            customer.setEmail("ivanov@mail.ru");
            customer.setStore(store);
            customerDAO.save(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

    private void customerReturnInventoryToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Rental rental = rentalDAO.getUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());
            rentalDAO.save(rental);

            session.getTransaction().commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0, 1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rental.setRentalDate(LocalDateTime.now());
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setCustomer(customer);
            payment.setStaff(staff);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setRental(rental);
            payment.setAmount(BigDecimal.valueOf(500.01));
            paymentDAO.save(payment);

            session.saveOrUpdate(customer);
            session.getTransaction().commit();
        }
    }

    private void newFilmWasMade() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().get();

            Set<Category> categories = categoryDAO.getItems(0, 5)
                    .stream()
                    .collect(Collectors.toSet());

            Set<Actor> actors = actorDAO.getItems(0, 20)
                    .stream()
                    .collect(Collectors.toSet());

            Film film = new Film();
            film.setLanguage(language);
            film.setActors(actors);
            film.setCategoryes(categories);
            film.setRating(Rating.NC17);
            film.setSpecialFeaturesFromEnumSet(Set.of(SpecialFeature.COMMENTARIES, SpecialFeature.TRAILERS));
            film.setLenght((short) 90);
            film.setReplacementCost(BigDecimal.valueOf(200.01));
            film.setRentalRate(BigDecimal.ZERO);
            film.setDescription("И за то, что мы делаем, отвечаем тоже вместе");
            film.setTitle("Бригада");
            film.setRentalDuration((byte) 3);
            film.setLanguage(language);
            film.setYear(Year.of(2025));
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setId(film.getId());
            filmText.setFilm(film);
            filmText.setDescription("И за то, что мы делаем, отвечаем тоже вместе");
            filmText.setTitle("Бригада");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }
}