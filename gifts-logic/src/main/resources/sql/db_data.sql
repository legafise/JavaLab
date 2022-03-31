INSERT INTO `gift_certificates`
VALUES (101, 'TattooLand', 'The certificate allows to you make a tattoo', 125.00, 92, '2022-01-20 21:00:00',
        '2022-04-20 21:00:00', 0),
       (102, 'Jump park', 'Free jumps at trampolines', 35.00, 30, '2022-03-15 21:30:00', '2022-06-15 21:30:00', 0),
       (103, 'Water park', 'Visit to the water park for 4 hours', 50.00, 30, '2022-02-10 15:45:00',
        '2022-05-10 15:45:00', 0);

INSERT INTO `tags`
VALUES (101, 'Tattoo'),
       (102, 'Jumps'),
       (103, 'Entertainment'),
       (104, 'Swimming');


INSERT INTO `gift_tags`
VALUES (101, 101),
       (102, 102),
       (102, 103),
       (102, 103);

INSERT INTO `users`
VALUES (101, 'Oleg', 100.00),
       (102, 'Ivan', 75.78);

INSERT INTO `orders`
VALUES (101, 101, 125.00, '2022-03-20 17:14:42'),
       (102, 102, 30.00, '2022-04-20 15:12:34'),
       (103, 103, 50.00, '2022-02-20 21:47:15');

INSERT INTO `user_orders`
VALUES (101, 101),
       (102, 102);