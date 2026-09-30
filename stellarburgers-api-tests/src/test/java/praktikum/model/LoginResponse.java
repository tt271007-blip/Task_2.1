package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    public boolean success;
    public String accessToken;
    public String refreshToken;
    public User user;
}