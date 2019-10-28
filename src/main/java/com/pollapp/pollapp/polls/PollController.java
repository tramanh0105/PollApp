package com.pollapp.pollapp.polls;

import com.pollapp.pollapp.payload.request.PollRequest;
import com.pollapp.pollapp.payload.response.PagedResponse;
import com.pollapp.pollapp.payload.response.PollResponse;
import com.pollapp.pollapp.security.CurrentUser;
import com.pollapp.pollapp.security.UserPrincipal;
import com.pollapp.pollapp.user.UserRepo;
import com.pollapp.pollapp.util.AppConstants;
import com.pollapp.pollapp.votes.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/api/polls")
public class PollController {
    @Autowired
    private PollRepo pollRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PollService pollService;

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        return pollService.getAllPolls(currentUser, page, size);
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest){
        Poll poll = pollService.createPoll(pollRequest);
        URI selfRefLink = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{pollId}").
                buildAndExpand(poll.getId()).toUri();
        return ResponseEntity.created(selfRefLink).body(poll);
    }
}
