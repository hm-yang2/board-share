package com.powerbi.api.service;

import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.SuperUserRepository;
import com.powerbi.api.model.ChannelRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelAdminRepository channelAdminRepository;
    private final ChannelOwnerRepository channelOwnerRepository;
    private final SuperUserRepository superUserRepository;

    @Autowired
    public PermissionService(
            ChannelMemberRepository channelMemberRepository,
            ChannelAdminRepository channelAdminRepository,
            ChannelOwnerRepository channelOwnerRepository,
            SuperUserRepository superUserRepository
    ) {
        this.channelMemberRepository = channelMemberRepository;
        this.channelAdminRepository = channelAdminRepository;
        this.channelOwnerRepository = channelOwnerRepository;
        this.superUserRepository = superUserRepository;
    }

    /**
     * Checks if the user is a superuser.
     *
     * @param user The user to check
     * @return true if the user is a superuser, false otherwise
     */
    @Transactional
    public boolean hasSuperUserPermission(User user) {
        return superUserRepository.existsByUserId(user.getId());
    }

    /**
     * Checks if the user has the required role in the channel.
     *
     * @param user The user to check
     * @param channelId The channel ID
     * @param requiredRole The required role (member, admin, owner, super user)
     * @return true if the user has the required role, false otherwise
     */
    @Transactional
    public boolean hasChannelPermission(User user, Long channelId, ChannelRole requiredRole) {
        return switch (requiredRole) {
            case MEMBER -> channelMemberRepository.existsByUserIdAndChannelId(user.getId(), channelId);
            case ADMIN -> channelAdminRepository.existsByUserIdAndChannelId(user.getId(), channelId);
            case OWNER -> channelOwnerRepository.existsByUserIdAndChannelId(user.getId(), channelId);
            case SUPER_USER ->
                    hasSuperUserPermission(user);  // You can also directly call the hasSuperUserPermission method here
            default -> false;
        };
    }

    @Transactional
    public ChannelRole getUserRoleInChannel(User user, Long channelId) {
        if (superUserRepository.existsByUserId(user.getId())) {
            return ChannelRole.SUPER_USER;
        }

        if (channelOwnerRepository.existsByUserIdAndChannelId(user.getId(), channelId)) {
            return ChannelRole.OWNER;
        }

        if (channelAdminRepository.existsByUserIdAndChannelId(user.getId(), channelId)) {
            return ChannelRole.ADMIN;
        }

        if (channelMemberRepository.existsByUserIdAndChannelId(user.getId(), channelId)) {
            return ChannelRole.MEMBER;
        }

        return ChannelRole.NOT_ALLOWED;
    }
}
