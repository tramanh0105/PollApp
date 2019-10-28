package com.pollapp.pollapp.polls;

import com.pollapp.pollapp.choiceVoteCount.ChoiceVoteCount;
import com.pollapp.pollapp.exception.BadRequestException;
import com.pollapp.pollapp.payload.request.PollRequest;
import com.pollapp.pollapp.payload.response.PagedResponse;
import com.pollapp.pollapp.payload.response.PollResponse;
import com.pollapp.pollapp.security.UserPrincipal;
import com.pollapp.pollapp.user.User;
import com.pollapp.pollapp.user.UserRepo;
import com.pollapp.pollapp.util.AppConstants;
import com.pollapp.pollapp.util.ModelMapper;
import com.pollapp.pollapp.votes.Vote;
import com.pollapp.pollapp.votes.VoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class PollService {
    @Autowired
    private PollRepo pollRepo;
    @Autowired
    private VoteRepo voteRepo;
    @Autowired
    private UserRepo userRepo;

    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page,size);

        //Retrieving Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
        Page <Poll> polls = pollRepo.findAll(pageable);
        if(polls.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), polls.getNumber(),
                    polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
        }

        // Mapping pollResponse into PageResponse
        List<Long> pollIds = polls.map(Poll::getId).getContent();
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());

        List<PollResponse> pollResponses = polls.map(poll -> {
            return ModelMapper.mapPollToPollResponse(poll,
                    choiceVoteCountMap,
                    creatorMap.get(poll.getCreatedBy()),
                    pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
        }).getContent();

        return new PagedResponse<>(pollResponses, polls.getNumber(),
                polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
    }

    public Poll createPoll(PollRequest pollRequest){
        Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());
        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));
        return pollRepo.save(poll);
    }

    private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
        // Retrieve Vote Counts of every Choice belonging to the given pollIds
        List<ChoiceVoteCount> votes = voteRepo.countByPollIdInGroupByChoiceId(pollIds);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        return choiceVotesMap;
    }

    private Map<Long, User> getPollCreatorMap(List<Poll> polls) {
        // Get Poll Creator details of the given list of polls
        List<Long> creatorIds = polls.stream()
                .map(Poll::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepo.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

    private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
        // Retrieve Votes done by the logged in user to the given pollIds
        Map<Long, Long> pollUserVoteMap = null;
        if(currentUser != null) {
            List<Vote> userVotes = voteRepo.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);

            pollUserVoteMap = userVotes.stream()
                    .collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
        }
        return pollUserVoteMap;
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0){
            throw new BadRequestException("Page number cannot be less than zero");
        }
        if(size > AppConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("Page size must not be greater than "+ AppConstants.MAX_PAGE_SIZE);
        }
    }
}
