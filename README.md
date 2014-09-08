ACCOUNT SERVICE
===============

DESCRIPTION
-----------

This is a simple Account Service that supports two main
operations:

* `getAmount(id)` returns an amount value for given id;
* `addAmount(id, value)` increment amount value by provided one.

Those methods are provided via RESTful API. All data is cached in RAM,
as well as persistently stored in Postgres DB. That caching schema
allows us to drastically boost up the read rate, as well as keep
it 100% durable.

Configurable load-test client is implemented. Number of readers and writers
is set from command-line arguments, as well as the range of load-tested IDs.

The server is tracking load statistics: total number of requests for
each method and average number of requests per second. Stats are
available via REST API as well.

SETTING UP
----------

Having the repo cloned into some local directory, one should
set up and configure Postgers database. Open the postgres
console (`sudo -u postgres psql`) and type in contents
of file `database.sql`. Note that after creation of database
and user one shall quit the console and log back in as the
newly created user.

That's all, folks. Use `./build.sh && ./runServer.sh` in order
to start the server. `./build.sh && ./runClient.sh` will start
the client.

REST API
--------

The following schema is used:

* `GET /accounts/:Id` returns the amount for provided ID
* `POST /accounts/:Id :Value` (value provided in the body of the request)
    adds the value to the account for the pointed id.
* `GET /accounts/statistics/addamount` returns JSON-formatted stats for `addAmount`
* `GET /accounts/statistics/getamount` returns JSON-formatted stats for `getAmount`
* `POST /accounts/resetstats` will, evidently, reset the stats.
