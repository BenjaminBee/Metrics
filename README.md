# Metrics

<pre>
#SQL Script:
DROP TABLE IF EXISTS METRICS;
DROP TABLE IF EXISTS ACCOUNTS;

CREATE TABLE METRICS (
	ID int AUTO_INCREMENT PRIMARY KEY,
    `SYSTEM` varchar(125),
    `NAME` varchar(125),
    `DATE` int,
    `VALUE` int
);

CREATE TABLE ACCOUNTS (
	ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    USERNAME varchar(125),
    EMAIL varchar(125),
    `PASSWORD` varchar(125)
);

#Instructions
Create new Database connection with the following settings:
HostName: Localhost
Username: root
Password: root

Create new schema called "metrics"

Run the SQL script given above.

Create a new workspace within postman
Create the following API url requests:
localhost:8080/account/register [POST]
localhost:8080/account/login [POST]
localhost:8080/metric/metrics?system=&name=&from=&to= [GET]
localhost:8080/metric/metrics/{id} [GET]
localhost:8080/metric/metrics [POST]
localhost:8080/metric/metrics/{id} [PUT]
localhost:8080/metric/metricsummary?system=&name=&from=&to= [GET]

</pre>
