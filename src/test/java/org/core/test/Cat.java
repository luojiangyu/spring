package org.core.test;

import org.core.beans.factory.annotation.Component;
import org.core.beans.factory.annotation.Scope;

@Component
@Scope("prototype")
public class Cat implements Animal{
    public void saySomething() {
		System.out.println("The cat say miao miao!");
		
	}

}
