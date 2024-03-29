package com.jev.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();

	private static int usersCount = 0;
	static {
		users.add(new User(++usersCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++usersCount, "Kev", LocalDate.now().minusYears(30)));
		users.add(new User(++usersCount, "Eve", LocalDate.now().minusYears(25)));
		users.add(new User(++usersCount, "Jim", LocalDate.now().minusYears(20)));
	}

	public List<User> findAll() {
		return users;
	}

	public User save(User user) {
		user.setId(++usersCount);
		users.add(user);
		return user;
	}

	public User findById(int id) {

		Predicate<? super User> predicate = user -> user.getId().equals(id); // will check if the id of user matches the
																				// id passed
		// the predicate then sends a list of users with given id
		// the stream filters the predicate and then fetches the first one and gets it.
		return users.stream().filter(predicate).findFirst().orElse(null); // if not found the gives back
	}

	public void deleteById(int id) {

		Predicate<? super User> predicate = user -> user.getId().equals(id); // will check if the id of user matches the
		users.removeIf(predicate);																		// id passed
		// the predicate then sends a list of users with given id
		// the stream filters the predicate and then fetches the first one and gets it.
	}
}
