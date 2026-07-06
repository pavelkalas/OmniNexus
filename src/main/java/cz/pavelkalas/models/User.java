package main.java.cz.pavelkalas.models;

public class User {
	private String name;
	private String password;
	public boolean _isLogged;

	public User(String name, String password) {
		this.name = name;
		this.password = password;
		this._isLogged = false;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean nameMatches(final String player) {
		return this.name.equalsIgnoreCase(player);
	}

	public boolean passwordMatches(final String password) {
		return this.password.equals(password);
	}

	public boolean valid() {
		return !name.contains("§") && !name.contains("§");
	}
}
