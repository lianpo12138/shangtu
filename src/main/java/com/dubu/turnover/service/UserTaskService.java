package com.dubu.turnover.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dubu.turnover.utils.EmailUtil;




@Service
public class UserTaskService {
	@Async
	public void sendMsg(String email, String title,String msg) {
		try{
			EmailUtil.send_email(email, title, msg);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
