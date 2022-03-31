package com.epam.esm.dao.extractor;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
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
public class OrderExtractorImpl implements ResultSetExtractor<List<Order>> {
    private static final String ORDER_PRICE = "order_price";
    private static final String PURCHASE_TIME = "purchase_time";
    private static final String ORDER_ID = "order_id";
    private final CertificateExtractorImpl certificateExtractor;

    @Autowired
    public OrderExtractorImpl(CertificateExtractorImpl certificateExtractor) {
        this.certificateExtractor = certificateExtractor;
    }

    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Order> orderList = new ArrayList<>();

        while (resultSet.next()) {
            Order order = new Order();
            fillMainOrderData(order, resultSet);
            resultSet.previous();
            order.setCertificate(certificateExtractor.extractData(resultSet).get(0));

            orderList.add(order);
        }

        return orderList;
    }

    public Order extractUserOrder(ResultSet resultSet, long mappingOrderId) throws SQLException {
        Order order = new Order();
        if (resultSet.getBigDecimal(ORDER_PRICE) != null) {
            fillMainOrderData(order, resultSet);
            order.setCertificate(certificateExtractor.extractUserOrderCertificate(resultSet, mappingOrderId));
        }

        return order;
    }

    private void fillMainOrderData(Order order, ResultSet resultSet) throws SQLException {
        order.setId(resultSet.getLong(ORDER_ID));
        order.setPrice(resultSet.getBigDecimal(ORDER_PRICE));
        order.setPurchaseTime(resultSet.getTimestamp(PURCHASE_TIME).toLocalDateTime());
    }
}
