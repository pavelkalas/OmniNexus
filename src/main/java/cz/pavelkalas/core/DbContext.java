package main.java.cz.pavelkalas.core;

import main.java.cz.pavelkalas.provider.DbUserProvider;

public class DbContext {
	/**
	 * Instance databáze registrovaných uživatelů.
	 */
	public static final DbUserProvider users = new DbUserProvider("users.txt");

	public DbContext() {
	}
}
