package com.pollapp.pollapp.choices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepo extends JpaRepository<Choice, Long> {
}
