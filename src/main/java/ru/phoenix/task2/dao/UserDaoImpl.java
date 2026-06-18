package ru.phoenix.task2.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.phoenix.task2.entity.User;
import ru.phoenix.task2.exception.DataBaseException;
import ru.phoenix.task2.util.HibernateUtil;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void save(User user) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataBaseException(e);
        }
    }

    @Override
    public User findById(Long id) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.get(User.class, id);

        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public List<User> findAll() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM User", User.class
            ).list();

        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public void update(User user) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataBaseException(e);
        }
    }

    @Override
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataBaseException(e);
        }
    }
}