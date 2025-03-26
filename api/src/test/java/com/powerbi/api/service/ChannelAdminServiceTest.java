package com.powerbi.api.service;

import com.powerbi.api.model.*;
import com.powerbi.api.repository.*;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({ChannelAdminService.class, UserService.class, PermissionService.class})
class ChannelAdminServiceTest {

    @Autowired
    private ChannelAdminService channelAdminService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private ChannelAdminRepository channelAdminRepository;

    @Autowired
    private PermissionService permissionService;

    private User adminUser;
    private User regularUser;
    private Channel channel;

    @BeforeEach
    void setUp() {
        // Create test users
        adminUser = new User();
        adminUser.setEmail("adminUser");
        userRepository.save(adminUser);

        regularUser = new User();
        regularUser.setEmail("regularUser");
        userRepository.save(regularUser);

        // Create a test channel
        channel = new Channel();
        channel.setName("Test Channel");
        channelRepository.save(channel);

        // Assign admin role to adminUser
        ChannelAdmin channelAdmin = new ChannelAdmin();
        channelAdmin.setUser(adminUser);
        channelAdmin.setChannel(channel);
        channelAdminRepository.save(channelAdmin);
    }

    @Test
    void testGetChannelAdmins_AdminUser_ReturnsAdmins() {
        List<ChannelAdmin> admins = channelAdminService.getChannelAdmins(adminUser.getEmail(), channel.getId());
        assertNotNull(admins);
        assertEquals(1, admins.size());
        assertEquals(adminUser.getId(), admins.get(0).getUser().getId());
    }

    @Test
    void testGetChannelAdmins_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.getChannelAdmins(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testAddChannelAdmin_AdminUser_AddsAdmin() {
        ChannelAdmin newAdmin = channelAdminService.addChannelAdmin(adminUser.getEmail(), channel.getId(), regularUser.getId());
        assertNotNull(newAdmin);
        assertEquals(regularUser.getId(), newAdmin.getUser().getId());
        assertEquals(channel.getId(), newAdmin.getChannel().getId());
    }

    @Test
    void testAddChannelAdmin_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.addChannelAdmin(regularUser.getEmail(), channel.getId(), adminUser.getId()));
    }

    @Test
    void testAddChannelAdmin_UserAlreadyAdmin_ThrowsDataIntegrityViolationException() {
        // Add the user as an admin first
        channelAdminService.addChannelAdmin(adminUser.getEmail(), channel.getId(), regularUser.getId());

        // Try adding the same user again
        assertThrows(DataIntegrityViolationException.class, () ->
                channelAdminService.addChannelAdmin(adminUser.getEmail(), channel.getId(), regularUser.getId()));
    }

    @Test
    void testRemoveChannelAdmin_AdminUser_RemovesAdmin() {
        // Add an admin first
        ChannelAdmin newAdmin = channelAdminService.addChannelAdmin(adminUser.getEmail(), channel.getId(), regularUser.getId());

        // Remove the admin
        channelAdminService.removeChannelAdmin(adminUser.getEmail(), channel.getId(), newAdmin.getId());

        assertFalse(channelAdminRepository.existsById(newAdmin.getId()));
    }

    @Test
    void testRemoveChannelAdmin_RegularUser_ThrowsAccessDeniedException() {
        // Try removing the admin as a regular user
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.removeChannelAdmin(regularUser.getEmail(), channel.getId(), adminUser.getId()));
    }

    @Test
    void testRemoveChannelAdmin_AdminNotFound_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                channelAdminService.removeChannelAdmin(adminUser.getEmail(), channel.getId(), 999L));
    }

    @Test
    void testGetChannelMembers_AdminUser_ReturnsMembers() {
        // Add a channel member
        ChannelMember member = new ChannelMember();
        member.setChannel(channel);
        member.setUser(regularUser);
        channelMemberRepository.save(member);

        List<ChannelMember> members = channelAdminService.getChannelMembers(adminUser.getEmail(), channel.getId());
        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals(regularUser.getId(), members.get(0).getUser().getId());
    }

    @Test
    void testGetChannelMembers_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.getChannelMembers(regularUser.getEmail(), channel.getId()));
    }

    @Test
    void testAddChannelMember_AdminUser_AddsMember() {
        ChannelMember newMember = channelAdminService.addChannelMember(adminUser.getEmail(), channel.getId(), regularUser.getId());
        assertNotNull(newMember);
        assertEquals(regularUser.getId(), newMember.getUser().getId());
        assertEquals(channel.getId(), newMember.getChannel().getId());
    }

    @Test
    void testAddChannelMember_RegularUser_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.addChannelMember(regularUser.getEmail(), channel.getId(), adminUser.getId()));
    }

    @Test
    void testAddChannelMember_UserAlreadyMember_ThrowsDataIntegrityViolationException() {
        // Add the user as a member first
        channelAdminService.addChannelMember(adminUser.getEmail(), channel.getId(), regularUser.getId());

        // Try adding the same user again
        assertThrows(DataIntegrityViolationException.class, () ->
                channelAdminService.addChannelMember(adminUser.getEmail(), channel.getId(), regularUser.getId()));
    }

    @Test
    void testRemoveChannelMember_AdminUser_RemovesMember() {
        // Add a channel member
        ChannelMember member = channelAdminService.addChannelMember(adminUser.getEmail(), channel.getId(), regularUser.getId());

        // Remove the member
        channelAdminService.removeChannelMember(adminUser.getEmail(), channel.getId(), member.getId());

        assertFalse(channelMemberRepository.existsById(member.getId()));
    }

    @Test
    void testRemoveChannelMember_RegularUser_ThrowsAccessDeniedException() {
        // Add a channel member
        ChannelMember member = channelAdminService.addChannelMember(adminUser.getEmail(), channel.getId(), regularUser.getId());

        // Try removing the member as a regular user
        assertThrows(AccessDeniedException.class, () ->
                channelAdminService.removeChannelMember(regularUser.getEmail(), channel.getId(), member.getId()));
    }

    @Test
    void testRemoveChannelMember_MemberNotFound_ThrowsException() {
        assertThrows(RuntimeException.class, () ->
                channelAdminService.removeChannelMember(adminUser.getEmail(), channel.getId(), 999L));
    }
}
