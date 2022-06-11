import http from "Connection";

class Services {
  login(data) {
    return http.post("/login", data);
  }
  signup(data) {
    return http.post("/signup", data);
  }
  getByUsername(username) {
    return http.get(`/user/${username}`);
  }
  updatePlate(username, data) {
    return http.put(`/plates/${username}`, data);
  }
  updateMoney(username, data) {
    return http.put(`/money/${username}`, data);
  }
}

export default new Services();
