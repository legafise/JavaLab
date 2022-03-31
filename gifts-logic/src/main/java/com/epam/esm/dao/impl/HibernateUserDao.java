package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Profile({"prod", "hibernate-test"})
public class HibernateUserDao implements UserDao {
    private static final String ADD_ORDER_TO_USER_QUERY = "INSERT INTO user_orders (user_id, order_id) VALUES (?, ?)";
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findAll(int page, int pageSize) {
        entityManager.unwrap(Session.class).clear();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        TypedQuery<User> findAllQuery = entityManager.createQuery(query);
        findAllQuery.setFirstResult((page - 1) * pageSize);
        findAllQuery.setMaxResults(pageSize);

        return findAllQuery.getResultList();
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public boolean addOrderToUser(long userId, long orderId) {
        Query addOrderToUserQuery = entityManager.createNativeQuery(ADD_ORDER_TO_USER_QUERY);
        addOrderToUserQuery.setParameter(1, userId);
        addOrderToUserQuery.setParameter(2, orderId);

        return addOrderToUserQuery.executeUpdate() >= 1;
    }
}
