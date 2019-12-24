package com.mistermicheels.cymi.component.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.common.error.ForbiddenAccessException;
import com.mistermicheels.cymi.component.user.User;
import com.mistermicheels.cymi.component.user.UserService;

@Service
public class GroupService {

    private final UserService userService;
    private final GroupRepository repository;

    private final GroupMembershipRepository membershipRepository;

    @Autowired
    public GroupService(UserService userService, GroupRepository repository,
            GroupMembershipRepository membershipRepository) {
        this.userService = userService;
        this.repository = repository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public Group createGroup(String name, Long currentUserId, String currentUserDisplayName) {
        Group group = new Group(name);
        User currentUser = this.userService.getReference(currentUserId);

        GroupMembership membership = new GroupMembership(group, currentUser,
                GroupMembershipRole.Admin, currentUserDisplayName);

        this.repository.save(group);
        this.membershipRepository.save(membership);

        return group;
    }

    public GroupMembership findMembershipWithGroupByGroupAndUserIdOrThrow(Long id,
            Long currentUserId) {
        GroupMembership membership = this.membershipRepository
                .findWithGroupByGroupIdAndUserId(id, currentUserId).orElseThrow(
                        () -> new ForbiddenAccessException("You are not a member of this group"));

        return membership;
    }

    public List<GroupMembership> findMembershipsWithGroupsByUserId(Long userId) {
        return this.membershipRepository.findWithGroupsByUserId(userId);
    }

}
