import http from "./Connection";

class Services {
  login(id) {
    return http.get(`/drivers/${id}`);
  }
  getRequestsByDriver(id) {
    return http.get(`/requests/${id}`);
  }
  updateStatus(data) {
    return http.put("/requests", data);
  }
  getPoints(id) {
    return http.get(`/userpoints/${id}`);
  }
  updatePoints(data) {
    return http.put("/userpoints", data);
  }
}

export default new Services();
