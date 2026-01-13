package com.example.learn_spring_framework.repoTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IUserRepository;

@DataJpaTest
@DisplayName("Tests for UserRepository")
public class IUserRepositoryTest {
	
	@Autowired private IUserRepository userRepository;
	
	//Helper to work with entities in test
	@Autowired private TestEntityManager entityManager;
	
	@Test
	@DisplayName("find by user name: return user successfully!")
	void findByUserName_shouldReturnUser() {
		User user = new User("A", "pass123", Set.of());
		entityManager.persist(user); //save a new entity object that managed by JPA
		entityManager.flush(); //update changes (insert, update, delete) from RAM to database instead of rollback
		
	
		Optional<User> found = userRepository.findByUserName("A");
		
		assertThat(found).isPresent();
		assertThat(found.get().getUserName()).isEqualTo("A");
	}
	
	@Test
	@DisplayName("exists by user name: return true if exists!")
    void existsByUserName_shoudReturnsTrueIfExists() {
        User user = new User("B", "pass123", Set.of());
        entityManager.persist(user);

        assertThat(userRepository.existsByUserName("B")).isTrue();
        assertThat(userRepository.existsByUserName("A")).isFalse();
    }
}
