package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponse {
    public boolean success;
    public User user;
    public String accessToken;
    public String refreshToken;
}