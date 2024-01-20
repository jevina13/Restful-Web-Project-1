package com.jev.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {
	
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service=service;
	}
	
	//GET /users
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return service.findAll();
	}
	
	//GET /users/{id}
	@GetMapping("/users/{id}")
	public User retrieveUserId(@PathVariable int id){
		User user = service.findById(id);
		if(user == null)
			throw new UserNotFoundException("id: "+ id);
		return user;
	}
	
	//POST /users
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = service.save(user);
		// to get the user 4 saved and passed in url
		//  /users/4 => /users/{id},  user.getId
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()      //to the uri of surrent req					
				.path("/{id}")												// add path {id}
				.buildAndExpand(savedUser.getId())							//which is saved in the user
				.toUri();													//and convert it into URI
		//location - /users/4
		return ResponseEntity.created(location).build();					//201 - Created
	}

}
