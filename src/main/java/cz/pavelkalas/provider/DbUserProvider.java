package main.java.cz.pavelkalas.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DbUserProvider {
	private final String dbFile;

	public static class User {
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

	private final ArrayList<User> users = new ArrayList<User>();

	public DbUserProvider(final String dbFile) {
		this.dbFile = dbFile;
		if (dbFile == null) {
			throw new IllegalArgumentException("DB_FILE cannot be null.");
		}
		load();
	}

	public void changePassword(String name, String password) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}

		if (password.contains("§") || password == null) {
			throw new IllegalArgumentException("Password cannot contain § symbol or be null.");
		}

		User user = get(name);
		if (user != null) {
			user.setPassword(password);
			save();
		}
	}

	public User get(String name) {
		if (name == null) {
			throw new RuntimeException("Name cannot be null!");
		}

		for (User u : users) {
			if (u.nameMatches(name)) {
				return u;
			}
		}

		return null;
	}

	public boolean add(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null!");
		}

		if (!user.valid()) {
			throw new RuntimeException("User cannot have symbol § in their credentials!");
		}

		boolean ok = users.add(user);
		save();
		return ok;
	}

	public void remove(String name) {
		int index = 0;
		boolean found = false;
		for (int i = 0; i < users.size(); i++) {
			index = i;
			if (users.get(i).nameMatches(name)) {
				found = true;
				break;
			}
		}
		if (found) {
			users.remove(index);
			save();
		}
	}

	private String getDb() {
		String dbContext = "";

		for (User u : users) {
			dbContext += "#user=" + u.getName() + "§" + u.getPassword() + "\n";
		}

		return dbContext.trim();
	}

	private void load() {
		if (dbFile == null) {
			throw new RuntimeException("DB_FILE is null!");
		}

		File file = new File(dbFile);

		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#")) {
					String rawCredentials = line.substring(6).trim();
					if (rawCredentials.contains("§")) {
						String[] args = rawCredentials.split("§");
						String name = args[0].trim();
						String password = args[1].trim();
						users.add(new User(name, password));
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (scanner != null) {
			scanner.close();
		}
	}

	public boolean exists(String name) {
		if (name == null) {
			throw new RuntimeException("Name cannot be null!");
		}

		return get(name) != null;
	}

	public boolean save() {
		if (dbFile == null) {
			throw new RuntimeException("DB_FILE is null!");
		}

		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		String dbContent = getDb();

		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(dbContent);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}