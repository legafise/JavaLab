package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.extractor.OrderExtractorImpl;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile({"template", "template-test"})
public class JdbcTemplateOrderDao implements OrderDao {
    private static final String FIND_ORDER_BY_ID_SQL = "SELECT orders.id AS order_id, orders.price AS order_price, orders.purchase_time, gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.is_deleted, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name FROM orders LEFT JOIN gift_certificates ON orders.certificate_id = gift_certificates.id" +
            " LEFT JOIN gift_tags ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id WHERE orders.id = ?";
    private static final String ADD_ORDER_SQL = "INSERT INTO orders (certificate_id, price, purchase_time) VALUES (?, ?, ?)";
    private static final String FIND_MAX_ORDER_ID = "SELECT MAX(id) FROM orders";
    private final OrderExtractorImpl orderExtractor;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateOrderDao(OrderExtractorImpl orderExtractor, JdbcTemplate jdbcTemplate) {
        this.orderExtractor = orderExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Order> findById(long id) {
        List<Order> orderList = jdbcTemplate.query(FIND_ORDER_BY_ID_SQL, orderExtractor, id);

        return orderList == null || orderList.isEmpty() ? Optional.empty()
                : Optional.of(orderList.get(0));
    }

    @Override
    public void add(Order order) {
        jdbcTemplate.update(ADD_ORDER_SQL, order.getCertificate().getId(), order.getPrice(), order.getPurchaseTime());
    }

    @Override
    public long findMaxOrderId() {
        return jdbcTemplate.queryForObject(FIND_MAX_ORDER_ID, Long.class);
    }
}
