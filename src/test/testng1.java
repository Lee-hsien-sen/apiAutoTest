package test;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class testng1 {

	@DataProvider(name = "user")
	public Object[][] createUser(Method m) {
		System.out.println(m.getName());
		return new Object[][] { { "root", "root" }, { "test", "root" }, };
	}

	@Test(groups = "login", dependsOnGroups = "launch", dataProvider = "user")
	public void verifyUser(String username, String password) {
		System.out.println("Verify User : " + username + ":" + password);
		assert username.equals(password);
	}
}
