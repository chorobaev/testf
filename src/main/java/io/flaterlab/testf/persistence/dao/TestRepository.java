package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.persistence.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {

    Optional<Test> findTestByTitle(String title);

    Optional<Test> findTestById(long id);

    Optional<Test> findTestByTitleAndUser(String title, User user);

    List<Test> findAllByPublishedTrue(Pageable pageable);

}
