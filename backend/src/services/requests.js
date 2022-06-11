const bodyParser = require('body-parser');
const connection = require('../config/db');
const { find } = require('geo-tz');
var moment = require('moment-timezone');

const createNewRequest = (req, res) => {
    const { body } = req;

    if (
        !body.userId ||
        !body.driverId ||
        !body.lat ||
        !body.lon ||
        !body.status ||
        !body.wasteWeight ||
        !body.wasteType
    ) {
        res.status(400).send({
            status: 'FAILED',
            data: {
                error: "One of the following keys is missing or is empty in request body: 'userId', 'driverId', 'lat', 'lon', 'status', 'wasteWeight', 'wasteType'",
            },
        });
        return;
    }

    const location = find(body.lat, body.lon);
    const request_time = moment
        .tz(location[0])
        .format('YYYY-MM-DD  HH:mm:ss.000');
    const pickup_time = moment
        .tz(location[0])
        .add(3, 'hours')
        .format('YYYY-MM-DD  HH:mm:ss.000');

    const newRequest = {
        userId: body.userId,
        driverId: body.driverId,
        lat: body.lat,
        lon: body.lon,
        status: body.status,
        request_time: request_time,
        pickup_time: pickup_time,
        wasteWeight: body.wasteWeight,
        wasteType: body.wasteType,
    };

    let sql = `INSERT INTO cuanpah.requests (userId, driverId, lat, lon, status, requestTime, pickupTime, wasteWeight, wasteType)
     VALUES (\'${newRequest.userId}\', \'${newRequest.driverId}\', \'${newRequest.lat}\', \'${newRequest.lon}\', \'${newRequest.status}\', \'${newRequest.request_time}\', \'${newRequest.pickup_time}\', \'${newRequest.wasteWeight}\', \'${newRequest.wasteType}\')`;

    connection.query(sql, (err) => {
        if (err) throw err;
        console.log('1 record inserted');
    });

    res.status(201).send({ status: 'OK', data: { newRequest } });
};

const getAllRequests = (req, res) => {
    try {
        let sql = `SELECT * FROM requests`;
        const { user } = req.query;
        // "user" params
        if (user) {
            sql = `SELECT * FROM requests WHERE userId= ${user}`;
            connection.query(sql, (err, results) => {
                res.status(200).send({ status: 'OK', data: results });
            });
            return;
        }
        // default
        connection.query(sql, (err, results) => {
            res.status(200).send({ status: 'OK', data: results });
        });
    } catch (error) {
        console.log(error);
    }
};

module.exports = { createNewRequest, getAllRequests };
