package com.jev.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jev.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.jev.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	// private UserDaoService service;
	private UserRepository userrepository;
	private PostRepository postrepository;

	public UserJpaResource(UserRepository userrepository, PostRepository postrepository) {
		this.postrepository = postrepository;
		this.userrepository = userrepository;
	}

	// GET /users
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userrepository.findAll();
	}

	// GET /users/{id}
	/*
	 * @GetMapping("/users/{id}") public User retrieveUserId(@PathVariable int id){
	 * User user = service.findById(id); if(user == null) throw new
	 * UserNotFoundException("id: "+ id); return user; }
	 */

	// http://localhost:8080/users
//EntityModel
//WebMvcLinkBuilder

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = userrepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		EntityModel<User> entityModel = EntityModel.of(user.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	// DELETE /users/{id}
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUserId(@PathVariable int id) {
		userrepository.deleteById(id);
	}

	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id) {
		Optional<User> user = userrepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		return user.get().getPosts();
	}

	// POST /users
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userrepository.save(user);
		// to get the user 4 saved and passed in url
		// /users/4 => /users/{id}, user.getId
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // to the uri of surrent req
				.path("/{id}") // add path {id}
				.buildAndExpand(savedUser.getId()) // which is saved in the user
				.toUri(); // and convert it into URI
		// location - /users/4
		return ResponseEntity.created(location).build(); // 201 - Created
	}

	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = userrepository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		post.setUser(user.get());

		Post savedPost = postrepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // to the uri of surrent req
				.path("/{id}") // add path {id}
				.buildAndExpand(savedPost.getId()) // which is saved in the user
				.toUri();

		// location - /users/4
		return ResponseEntity.created(location).build(); // 201 - Created
	}

}
