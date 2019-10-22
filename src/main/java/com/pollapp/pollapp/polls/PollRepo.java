package com.pollapp.pollapp.polls;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepo extends JpaRepository<Poll, Long> {
    Page<Poll> findByCreatedBy(Long userId, Pageable pageable);
    long countByCreatedBy(Long userId);
    List<Poll> findByIdIn (List<Long> pollIds);
    List<Poll> findByIdIn (List<Long> pollIds, Sort sort);

}
