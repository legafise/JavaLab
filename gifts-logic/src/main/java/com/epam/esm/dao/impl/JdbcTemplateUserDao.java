package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.extractor.UserExtractorImpl;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile({"template", "template-test"})
public class JdbcTemplateUserDao implements UserDao {
    private static final String ADD_ORDER_TO_USER_SQL = "INSERT INTO user_orders (user_id, order_id) VALUES (?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET users.login = ?, users.balance = ? WHERE users.id = ?";
    private static final String FIND_ALL_USERS_SQL = "SELECT users.id AS user_id, users.login, users.balance, orders.id AS order_id, orders.price AS order_price, orders.purchase_time, gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name, gift_certificates.is_deleted FROM users LEFT JOIN user_orders ON users.id = user_orders.user_id" +
            " LEFT JOIN orders ON user_orders.order_id = orders.id LEFT JOIN gift_certificates ON orders.certificate_id = gift_certificates.id" +
            " LEFT JOIN gift_tags ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id ORDER BY" +
            " users.id ASC LIMIT ? OFFSET ?";
    private static final String FIND_USER_BY_ID_SQL = "SELECT users.id AS user_id, users.login, users.balance, orders.id AS order_id, orders.price AS order_price, orders.purchase_time, gift_certificates.id AS certificate_id," +
            " gift_certificates.name AS gift_certificate_name, gift_certificates.description, gift_certificates.price AS certificate_price," +
            " gift_certificates.duration, gift_certificates.create_date, gift_certificates.last_update_date, tags.id AS" +
            " tag_id, tags.name AS tag_name FROM users LEFT JOIN user_orders ON users.id = user_orders.user_id" +
            " LEFT JOIN orders ON user_orders.order_id = orders.id LEFT JOIN gift_certificates ON orders.certificate_id = gift_certificates.id" +
            " LEFT JOIN gift_tags ON gift_certificates.id = gift_tags.certificate_id LEFT JOIN tags ON gift_tags.tag_id = tags.id WHERE users.id = ?";
    private final UserExtractorImpl userExtractor;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateUserDao(UserExtractorImpl userExtractor, JdbcTemplate jdbcTemplate) {
        this.userExtractor = userExtractor;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(long id) {
        List<User> userList = jdbcTemplate.query(FIND_USER_BY_ID_SQL, userExtractor, id);

        return userList == null || userList.isEmpty() ? Optional.empty()
                : Optional.of(userList.get(0));
    }

    @Override
    public List<User> findAll(int page, int pageSize) {
        return jdbcTemplate.query(FIND_ALL_USERS_SQL, new Object[]{pageSize, (page - 1) * pageSize}, userExtractor);
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE_USER, user.getLogin(), user.getBalance(), user.getId());
        return jdbcTemplate.query(FIND_USER_BY_ID_SQL, userExtractor, user.getId()).get(0);
    }

    @Override
    public boolean addOrderToUser(long userId, long orderId) {
        return jdbcTemplate.update(ADD_ORDER_TO_USER_SQL, userId, orderId) >= 1;
    }
}
