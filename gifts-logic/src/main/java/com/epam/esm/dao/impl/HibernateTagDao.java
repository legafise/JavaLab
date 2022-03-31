package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@Profile({"prod", "hibernate-test"})
public class HibernateTagDao implements TagDao {
    private static final String REMOVE_TAG_FROM_CERTIFICATES_BY_ID_SQL = "DELETE FROM gift_tags WHERE tag_id = ?";
    private static final String FIND_WIDELY_USED_TAG = "SELECT tag_id FROM (SELECT tags.id AS tag_id, SUM(orders.price)" +
            " AS tag_orders_price FROM orders INNER JOIN gift_certificates ON orders.certificate_id = gift_certificates.id" +
            " LEFT JOIN gift_tags ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON tags.id = gift_tags.tag_id" +
            " GROUP BY tag_id ORDER BY tag_orders_price DESC LIMIT 1) A";
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(Tag tag) {
        tag.setId(0);
        entityManager.persist(tag);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> rootEntry = criteriaQuery.from(Tag.class);
        criteriaQuery.select(rootEntry).where(criteriaBuilder.equal(rootEntry.get("name"), name));
        TypedQuery<Tag> nameQuery = entityManager.createQuery(criteriaQuery);
        return nameQuery.getResultList().stream()
                .findFirst();
    }

    @Override
    public List<Tag> findAll(int page, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> rootEntry = criteriaQuery.from(Tag.class);
        criteriaQuery.select(rootEntry);
        TypedQuery<Tag> allTagsQuery = entityManager.createQuery(criteriaQuery);
        allTagsQuery.setFirstResult((page - 1) * pageSize);
        allTagsQuery.setMaxResults(pageSize);
        return allTagsQuery.getResultList();
    }

    @Override
    public Tag update(Tag tag) {
        return entityManager.merge(tag);
    }

    @Override
    public void remove(long id) {
        Tag removingTag = entityManager.find(Tag.class, id);
        entityManager.remove(removingTag);
    }

    @Override
    public void removeTagFromCertificates(long id) {
        Query nativeQuery = entityManager.createNativeQuery(REMOVE_TAG_FROM_CERTIFICATES_BY_ID_SQL);
        nativeQuery.setParameter(1, id);
        nativeQuery.executeUpdate();
    }

    @Override
    public Tag findWidelyUsedTag() {
        BigInteger widelyUsedTagId = (BigInteger) entityManager.createNativeQuery(FIND_WIDELY_USED_TAG).getResultList().get(0);
        return entityManager.find(Tag.class, widelyUsedTagId.longValue());
    }
}
