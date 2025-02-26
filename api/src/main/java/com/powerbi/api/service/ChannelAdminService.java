package com.powerbi.api.service;

import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelAdmin;
import com.powerbi.api.model.ChannelMember;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.repository.UserRepository;
import com.powerbi.api.util.ChannelRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelAdminService {
    private final PermissionService permissionService;

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelAdminRepository channelAdminRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChannelAdminService(
            PermissionService permissionService,
            ChannelRepository channelRepository,
            ChannelMemberRepository channelMemberRepository,
            ChannelAdminRepository channelAdminRepository,
            UserRepository userRepository
    ) {
        this.permissionService = permissionService;
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.channelAdminRepository = channelAdminRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean isAdminOrAbove(User user, Long channelId) {
        ChannelRole role = permissionService.getUserRoleInChannel(user, channelId);
        return role == ChannelRole.ADMIN || role == ChannelRole.OWNER || role == ChannelRole.SUPER_USER;
    }

    @Transactional
    public List<ChannelMember> getChannelMembers(User user, Long channelId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to view channel members.");
        }

        return channelMemberRepository.findByChannelId(channelId);
    }

    @Transactional
    public ChannelMember addChannelMember(User user, Long channelId, Long newUserId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to add members.");
        }

        Optional<User> member = userRepository.findById(newUserId);
        Optional<Channel> channel = channelRepository.findById(channelId);

        // Check if the user is already a member
        if (channelMemberRepository.existsByUserIdAndChannelId(newUserId, channelId)) {
            throw new DataIntegrityViolationException("User is already a member of this channel.");
        }

        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannel(channel.orElseThrow());
        channelMember.setUser(member.orElseThrow());

        return channelMemberRepository.save(channelMember);
    }

    @Transactional
    public void removeChannelMember(User user, Long channelId, Long memberId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to remove members.");
        }

        Optional<ChannelMember> member = channelMemberRepository.findById(memberId);

        channelMemberRepository.delete(member.orElseThrow());
    }

    @Transactional
    public List<ChannelAdmin> getChannelAdmins(User user, Long channelId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to view channel admins.");
        }

        return channelAdminRepository.findByChannelId(channelId);
    }

    @Transactional
    public void addChannelAdmin(User user, Long channelId, Long userId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to add admins.");
        }

        Optional<User> userToAdd = userRepository.findById(userId);
        Optional<Channel> channel = channelRepository.findById(channelId);

        // Check if the user is already an admin
        if (channelAdminRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new DataIntegrityViolationException("User is already an admin of this channel.");
        }

        ChannelAdmin channelAdmin = new ChannelAdmin();
        channelAdmin.setChannel(channel.orElseThrow());
        channelAdmin.setUser(userToAdd.orElseThrow());
        channelAdminRepository.save(channelAdmin);
    }

    @Transactional
    public void removeChannelAdmin(User user, Long channelId, Long adminId) {
        if (!isAdminOrAbove(user, channelId)) {
            throw new AccessDeniedException("User is not authorized to remove admins.");
        }

        Optional<ChannelAdmin> admin = channelAdminRepository.findById(adminId);

        // Remove the admin
        channelAdminRepository.delete(admin.orElseThrow());
    }
}

