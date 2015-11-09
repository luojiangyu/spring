package org.core.test;

import org.core.beans.factory.annotation.Component;

@Component
public class LoginServiceImp implements LoginService{

	@Override
	public boolean checkPsw(String psw) {
		if(psw.equals("ljy")){
			return true;
		}
		return false;
	}

}
