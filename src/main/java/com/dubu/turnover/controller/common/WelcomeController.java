package com.dubu.turnover.controller.common;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dubu.turnover.annotation.NoLogin;
import com.dubu.turnover.component.redis.RedisClient;

@RestController
@RequestMapping(value = "/")
public class WelcomeController {
	@Resource
	private RedisClient redisClient;
	@GetMapping 
	public String welcome() throws Exception {
			return "welcome visit priority service api";
	}
	
}
