package org.core.test;

import org.core.beans.factory.annotation.Autowired;
import org.core.beans.factory.annotation.Component;
import org.core.beans.factory.annotation.Value;

@Component("login")
public class LoginController {
	@Autowired
    private LoginService loginService;
	@Value("ljy")
	private String password;
	@Value("big sun")
	private String userName;
	public void login(){
		if(loginService.checkPsw(password)){
			System.out.println("用户"+userName+" 正在登陆中...");
		}
	}
}
