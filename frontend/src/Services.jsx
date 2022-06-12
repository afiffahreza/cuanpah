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
}

export default new Services();
