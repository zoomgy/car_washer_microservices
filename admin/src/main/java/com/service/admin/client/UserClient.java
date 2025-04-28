package com.service.admin.client;

import com.service.admin.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface UserClient {
    @GetExchange("/user/all")
    public ResponseEntity<List<User>> getAllUsers();
}
