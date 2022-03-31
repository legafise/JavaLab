package com.epam.esm.dao.extractor;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"template", "template-test"})
public class UserExtractorImpl implements ResultSetExtractor<List<User>> {
    private static final String USER_ID = "user_id";
    private static final String USER_LOGIN = "login";
    private static final String USER_BALANCE = "balance";
    private static final String ORDER_ID = "order_id";
    private final OrderExtractorImpl orderExtractor;

    @Autowired
    public UserExtractorImpl(OrderExtractorImpl orderExtractor) {
        this.orderExtractor = orderExtractor;
    }

    @Override
    public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<User> userList = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong(USER_ID));
            user.setLogin(resultSet.getString(USER_LOGIN));
            user.setBalance(resultSet.getBigDecimal(USER_BALANCE));
            user.setOrders(mapUserOrders(resultSet));

            userList.add(user);
        }

        return userList;
    }

    private List<Order> mapUserOrders(ResultSet resultSet) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        long mappingUserId = resultSet.getLong(USER_ID);

        while (!resultSet.isAfterLast() && resultSet.getLong(USER_ID) == mappingUserId) {
            long mappingOrderId = resultSet.getLong(ORDER_ID);
            orderList.add(orderExtractor.extractUserOrder(resultSet, mappingOrderId));
            resultSet.next();
        }

        return orderListChecker(orderList);
    }

    private List<Order> orderListChecker(List<Order> orderList) {
        return orderList.size() == 1 && orderList.stream()
                .findFirst().get().getPrice() == null ? new ArrayList<>() : orderList;
    }
}
