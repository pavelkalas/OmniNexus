package main.java.cz.pavelkalas.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.cz.pavelkalas.models.User;

/**
 * Jednoduchá custom databáze pro hráče.
 */
public class DbUserProvider {
	/**
	 * Cesta k databázi
	 */
	private final String dbFile;

	/**
	 * List registrovaných uživatelů.
	 */
	private final ArrayList<User> users = new ArrayList<User>();

	/**
	 * Inicializuje databázi.
	 * 
	 * @param dbFile - Soubor databáze
	 */
	public DbUserProvider(final String dbFile) {
		this.dbFile = dbFile;
		if (dbFile == null) {
			throw new IllegalArgumentException("DB_FILE cannot be null.");
		}
		load();
	}

	/**
	 * Změní uživatelovi heslo
	 * 
	 * @param name     - Uživatel
	 * @param password - Nové heslo
	 */
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

	/**
	 * Navrátí uživatele v případě, že byl nalezen.
	 * 
	 * @param name - Jméno uživatele
	 * @return - Instance uživatele pokud nalezen, jinak NULL.
	 */
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

	/**
	 * Přidá uživatele do databáze
	 * 
	 * @param user - Instance uživatele
	 * @return - Navrací TRUE v případě, že se úspěšně přidal bez chyb.
	 */
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

	/**
	 * Odstraní uživatele z databáze na základě jména, pokud existuje.
	 * 
	 * @param name - Jméno užiatele
	 */
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

	/**
	 * Načítá existující databázi uživatelů do listu.
	 */
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

	/**
	 * Kontroluje existenci uživatele podle jména.
	 * 
	 * @param name - Jméno uživatele
	 * @return Navrací TRUE pokud uživatel existuje, jinak FALSE.
	 */
	public boolean exists(String name) {
		if (name == null) {
			throw new RuntimeException("Name cannot be null!");
		}

		return get(name) != null;
	}

	/**
	 * Ukládá databázi do souboru.
	 * 
	 * @return - Navrací TRUE pokud uložení proběhlo úspěšně
	 */
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

		String dbContent = "";

		for (User u : users) {
			dbContent += "#user=" + u.getName() + "§" + u.getPassword() + "\n";
		}

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
