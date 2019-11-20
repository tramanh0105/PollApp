package com.pollapp.pollapp.choices;

import com.pollapp.pollapp.polls.Poll;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "choices")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 40)
    private String text;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Poll poll;

    public Choice() {
    }

    public Choice(@NotNull @Size(max = 40) String text) {
        this.text = text;
    }

    public Choice(@NotNull @Size(max = 40) String text, Poll poll) {
        this.text = text;
        this.poll = poll;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

   /* @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Choice choice = (Choice) obj;
        return Objects.equals(id, choice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }*/
}
