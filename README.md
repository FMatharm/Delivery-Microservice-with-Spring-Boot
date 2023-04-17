<img src="https://4.bp.blogspot.com/-ou-a_Aa1t7A/W6IhNc3Q0gI/AAAAAAAAD6Y/pwh44arKiuM_NBqB1H7Pz4-7QhUxAgZkACLcBGAs/s1600/spring-boot-logo.png" width="300">

<img src="https://uploads-ssl.webflow.com/62c752ed30acd6f385f98c0d/62c7a51a726af173a4db3341_logo-keycloak.png" width="300">

# Delivery Microservice with Spring Boot and Keycloak

This project provides a Delivery, Inventory and Order Microservice which has an embedded Keycloak Server, uses H2 as its RDBMS and Swagger for documentation on the API.

## Compatibility

|Spring Boot | Keycloak |
|------------|----------|
|2.6.7       | 18.0.0   |

## Usage

You can store a different **Products** for inventory, manage shopping carts for different **Users** and their orders with address and payment methods.

## API

This Microservice comprehends three REST controllers for **Products**, **Carts** and **Orders**. Calls can be directed to the following endpoints:

- http://localhost:8000/api/v1/products
- http://localhost:8000/api/v1/carts
- http://localhost:8000/api/v1/orders

These are secured with a JWT token provided by Keycloak on the following endpoint:

- http://localhost:8000/auth/realms/deliveryapi/protocol/openid-connect/token

For convenience you can use the Postman collection located in the assets folder.

## Configuration

You can manage Keycloak realm configuration in:

- http://localhost:8000/auth

H2 console is enabled in:

- http://localhost:8000/h2-console