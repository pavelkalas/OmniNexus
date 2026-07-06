package main.java.cz.pavelkalas.core;

import main.java.cz.pavelkalas.provider.DbUserProvider;

/**
 * Databázový kontext, sloužící k přímému přistupování k databázi staticky.
 */
public class DbContext {
	/**
	 * Instance databáze registrovaných uživatelů.
	 */
	public static final DbUserProvider users = new DbUserProvider("users.txt");

	public DbContext() {
	}
}
