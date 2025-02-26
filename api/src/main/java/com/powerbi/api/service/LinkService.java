package com.powerbi.api.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.powerbi.api.dto.LinkDTO;
import com.powerbi.api.model.Link;
import com.powerbi.api.model.User;
import com.powerbi.api.repository.LinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> getUserLinks(User user) {
        return linkRepository.findByUser(user);
    }

    @Transactional
    public Link getUserLink(User user, Long linkId) {
        Optional<Link> link = linkRepository.findByUserAndId(user, linkId);
        if (link.isEmpty()) {
            throw new ResourceNotFoundException("Link not found/Does not belong to user. LinkId: " + linkId);
        }
        return link.get();
    }

    @Transactional
    public Link createUserLink(User user, LinkDTO linkDTO) {
        Link link = new Link();
        link.setUser(user);
        link.setLink(link.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    @Transactional
    public Link updateUserLink(User user, LinkDTO linkDTO) {
        Link link = getUserLink(user, linkDTO.getId());
        link.setLink(linkDTO.getLink());
        link.setTitle(linkDTO.getTitle());
        link.setDescription(linkDTO.getDescription());

        return linkRepository.save(link);
    }

    @Transactional
    public void deleteUserLink(User user, Long linkId) {
        Link link = getUserLink(user, linkId);
        linkRepository.delete(link);
    }
}
