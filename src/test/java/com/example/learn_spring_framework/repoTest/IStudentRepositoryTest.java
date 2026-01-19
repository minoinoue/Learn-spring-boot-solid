package com.example.learn_spring_framework.repoTest;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IStudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class IStudentRepositoryTest {

    @Autowired
    private IStudentRepository studentRepository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student("SV001", "Nguyen Van An", null);
        student1.setDeleted(false);
        
        student2 = new Student("SV002", "Trần Thị Bình", null);
        student2.setDeleted(true);

        studentRepository.save(student1);
        studentRepository.save(student2);
        /*using H2 test in RAM
         * 
         * -> Test the repository to see if the query is being executed correctly.
         * + can check SQL error, ensure data integrity when downloaded and retrieved
         */
    }

    @Test
    @DisplayName("Find all active student successful")
    void findAllByDeletedFalse_shouldReturnActiveStudents() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Student> result = studentRepository.findAllByDeletedFalse(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStudentId()).isEqualTo("SV001");
    }

    @Test
    @DisplayName("find by full name containing and not deleted successful")
    void findByFullNameContaining_shouldReturnMatchingStudents() {
        // Act
        Page<Student> result = studentRepository
            .findByFullNameContainingIgnoreCaseAndDeletedFalse("an", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFullName()).contains("An");
    }

    @Test
    @DisplayName("find student by id and not deleted successful")
    void findByStudentIdAndDeletedFalse_shouldReturnStudent() {
        // Act
        Optional<Student> found = studentRepository.findByStudentIdAndDeletedFalse("SV001");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Nguyen Van An");
    }

    @Test
    @DisplayName("test soft deleted all successful")
    void softDeleteAllStudents_shouldUpdateAllToDeleted() {
        // Act
        studentRepository.softDeleteAllStudents();

        // Assert
        Page<Student> activeStudents = studentRepository.findAllByDeletedFalse(PageRequest.of(0, 10));
        
        assertThat(activeStudents.getContent()).isEmpty();
    }
}
