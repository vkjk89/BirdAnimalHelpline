package org.birdhelpline.app.repository;


import org.birdhelpline.app.model.Role;
import org.birdhelpline.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

//@Repository("userRepository")
public interface UserRepository {//extends JpaRepository<User, Integer> {
    //User findByEmail(String email);
	//List<User> findByRole(Role role);
}
