package com.example.learn_spring_framework.repoTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
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
	
	private User userA;
	private User userB;
	
	@BeforeEach
	void setUp() {
		userA = new User("A", "pass123", Set.of());
		userB = new User("B", "pass123", Set.of());
		userRepository.save(userA); //use for insert & update
		userRepository.save(userB);
	}
	
	@Test
	@DisplayName("find by user name should return user successfully")
	void findByUserName_shouldReturnUser() {
		//entityManager.persist(userA); save a new entity object that managed by JPA (only for insert)
		entityManager.flush(); //update changes (insert, update, delete) from RAM to database instead of rollback
		
	
		Optional<User> found = userRepository.findByUserName("A");
		
		assertThat(found).isPresent();
		assertThat(found.get().getUserName()).isEqualTo("A");
	}
	
	@Test
	@DisplayName("exists by user name must return true if exists")
    void existsByUserName_shoudReturnsTrueIfExists() {
        //entityManager.persist(userB);

        assertThat(userRepository.existsByUserName("B")).isTrue();
        assertThat(userRepository.existsByUserName("C")).isFalse();
    }
}
