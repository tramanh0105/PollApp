package com.pollapp.pollapp.votes;

import com.pollapp.pollapp.choices.Choice;
import com.pollapp.pollapp.polls.Poll;
import com.pollapp.pollapp.user.User;

import javax.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Poll poll;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Choice choice;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    public Vote() {
    }

    public Vote(Poll poll, Choice choice, User user) {
        this.poll = poll;
        this.choice = choice;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
