package com.powerbi.api.service;

import com.powerbi.api.model.Channel;
import com.powerbi.api.model.ChannelAdmin;
import com.powerbi.api.model.ChannelMember;
import com.powerbi.api.model.ChannelOwner;
import com.powerbi.api.model.ChannelRole;
import com.powerbi.api.model.SuperUser;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.ChannelAdminRepository;
import com.powerbi.api.repository.ChannelMemberRepository;
import com.powerbi.api.repository.ChannelOwnerRepository;
import com.powerbi.api.repository.ChannelRepository;
import com.powerbi.api.repository.SuperUserRepository;
import com.powerbi.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({PermissionService.class})
class PermissionServiceTest {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuperUserRepository superUserRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private ChannelAdminRepository channelAdminRepository;

    @Autowired
    private ChannelOwnerRepository channelOwnerRepository;

    @Autowired
    private ChannelRepository channelRepository;

    private User superUser;
    private User ownerUser;
    private User adminUser;
    private User memberUser;
    private User regularUser;
    private Channel channel;

    @BeforeEach
    void setUp() {
        // Create users
        superUser = new User();
        superUser.setEmail("superUser");
        userRepository.save(superUser);

        ownerUser = new User();
        ownerUser.setEmail("ownerUser");
        userRepository.save(ownerUser);

        adminUser = new User();
        adminUser.setEmail("adminUser");
        userRepository.save(adminUser);

        memberUser = new User();
        memberUser.setEmail("memberUser");
        userRepository.save(memberUser);

        regularUser = new User();
        regularUser.setEmail("regularUser");
        userRepository.save(regularUser);

        // Create a channel
        channel = new Channel();
        channel.setName("Test Channel");
        // channel.setVisibility(Visibility.PRIVATE);
        channelRepository.save(channel);

        // Assign roles
        SuperUser superUserEntity = new SuperUser();
        superUserEntity.setUser(superUser);
        superUserRepository.save(superUserEntity);

        ChannelOwner channelOwner = new ChannelOwner();
        channelOwner.setUser(ownerUser);
        channelOwner.setChannel(channel);
        channelOwnerRepository.save(channelOwner);

        ChannelAdmin channelAdmin = new ChannelAdmin();
        channelAdmin.setUser(adminUser);
        channelAdmin.setChannel(channel);
        channelAdminRepository.save(channelAdmin);

        ChannelMember channelMember = new ChannelMember();
        channelMember.setUser(memberUser);
        channelMember.setChannel(channel);
        channelMemberRepository.save(channelMember);
    }

    @Test
    void testHasSuperUserPermission_ReturnsTrueForSuperUser() {
        assertTrue(permissionService.hasSuperUserPermission(superUser));
    }

    @Test
    void testHasSuperUserPermission_ReturnsFalseForNonSuperUser() {
        assertFalse(permissionService.hasSuperUserPermission(regularUser));
    }

    @Test
    void testHasChannelPermission_ReturnsTrueForOwner() {
        assertTrue(permissionService.hasChannelPermission(ownerUser, channel.getId(), ChannelRole.OWNER));
    }

    @Test
    void testHasChannelPermission_ReturnsTrueForAdmin() {
        assertTrue(permissionService.hasChannelPermission(adminUser, channel.getId(), ChannelRole.ADMIN));
    }

    @Test
    void testHasChannelPermission_ReturnsTrueForMember() {
        assertTrue(permissionService.hasChannelPermission(memberUser, channel.getId(), ChannelRole.MEMBER));
    }

    @Test
    void testHasChannelPermission_ReturnsFalseForRegularUser() {
        assertFalse(permissionService.hasChannelPermission(regularUser, channel.getId(), ChannelRole.MEMBER));
    }

    @Test
    void testHasChannelPermission_ReturnsTrueForSuperUser() {
        assertTrue(permissionService.hasChannelPermission(superUser, channel.getId(), ChannelRole.SUPER_USER));
    }

    @Test
    void testGetUserRoleInChannel_ReturnsSuperUserRole() {
        assertEquals(ChannelRole.SUPER_USER, permissionService.getUserRoleInChannel(superUser, channel.getId()));
    }

    @Test
    void testGetUserRoleInChannel_ReturnsOwnerRole() {
        assertEquals(ChannelRole.OWNER, permissionService.getUserRoleInChannel(ownerUser, channel.getId()));
    }

    @Test
    void testGetUserRoleInChannel_ReturnsAdminRole() {
        assertEquals(ChannelRole.ADMIN, permissionService.getUserRoleInChannel(adminUser, channel.getId()));
    }

    @Test
    void testGetUserRoleInChannel_ReturnsMemberRole() {
        assertEquals(ChannelRole.MEMBER, permissionService.getUserRoleInChannel(memberUser, channel.getId()));
    }

    @Test
    void testGetUserRoleInChannel_ReturnsNotAllowedForRegularUser() {
        assertEquals(ChannelRole.NOT_ALLOWED, permissionService.getUserRoleInChannel(regularUser, channel.getId()));
    }
}