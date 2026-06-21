package ru.phoenix.task2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY;

    static {
        try {
            SESSION_FACTORY = new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static SessionFactory buildSessionFactory(
            String url,
            String username,
            String password) {

        return new Configuration()
                .configure()
                .setProperty(
                        "hibernate.connection.url",
                        url
                )
                .setProperty(
                        "hibernate.connection.username",
                        username
                )
                .setProperty(
                        "hibernate.connection.password",
                        password
                )
                .setProperty(
                        "hibernate.hbm2ddl.auto",
                        "create-drop"
                )
                .buildSessionFactory();
    }

    public static void shutdown() {
        SESSION_FACTORY.close();
    }
}