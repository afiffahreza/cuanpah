# Cuanpah Backend
Frameworks used: Node.js 16, express.js, mysql  
Database deployed to Cloud SQL  
API deployed to Cloud Run  
API Link: https://cuanpah-backend-5xzqacjzkq-et.a.run.app 

## API Endpoints
User Endpoints
- POST /register
- POST /login  

Pickup Request Endpoints
- POST /requests
- GET /requests
- GET /requests/:driverId
- PUT /requests  

Points Endpoints
- PUT /userpoints
- POST /userpoints
- GET /userpoints/:userId  

Voucher Endpoints
- PUT /vouchers
- POST /vouchers
- GET /vouchers/:userId  

Driver Endpoints
- POST /drivers
- GET /drivers/:userId
