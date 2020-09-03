package com.demo.utlility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	File file;
	FileInputStream fis;
	Properties pro;

	public ConfigReader() {

		try {
			file = new File(System.getProperty("user.dir") + "/config/config.properties");
			fis = new FileInputStream(file);
			pro = new Properties();
			pro.load(fis);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String getUrl() {
		String url = pro.getProperty("baseUrl");
		return url;
	}

	public String getUsername() {
		String username = pro.getProperty("Username");
		return username;
	}

	public String getPassword() {
		String pass = pro.getProperty("Password");
		return pass;
	}
	
	public String getSuperAdminUsername() {
		String superAdminusername = pro.getProperty("superAdminUserName");
		return superAdminusername;
		
	}

	public String getSuperAdminPassword() {
		String superAdminpass = pro.getProperty("superAdminPassword");
		return superAdminpass;
		
	}

}
