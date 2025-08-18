package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.AnnouncementDTO;
import com.teafactory.pureleaf.entity.Announcement;
import com.teafactory.pureleaf.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement addAnnouncement(AnnouncementDTO dto, List<MultipartFile> attachments) {
        Announcement announcement = new Announcement();
        announcement.setTopic(dto.getTopic());
        announcement.setSubject(dto.getSubject());
        announcement.setContent(dto.getContent());
        announcement.setFactories(dto.getFactories());

        // Handle file uploads
        if (attachments != null && !attachments.isEmpty()) {
            List<String> filenames = attachments.stream().map(file -> {
                try {
                    String uploadDir = "uploads/";
                    File uploadFile = new File(uploadDir + file.getOriginalFilename());
                    file.transferTo(uploadFile);
                    return file.getOriginalFilename();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());
            announcement.setAttachments(filenames);
        }

        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id).orElse(null);
    }

    public Announcement updateAnnouncement(Long id, AnnouncementDTO dto, List<MultipartFile> attachments) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement == null) return null;
        announcement.setTopic(dto.getTopic());
        announcement.setSubject(dto.getSubject());
        announcement.setContent(dto.getContent());
        announcement.setFactories(dto.getFactories());
        // Handle file uploads (replace attachments if provided)
        if (attachments != null && !attachments.isEmpty()) {
            List<String> filenames = attachments.stream().map(file -> {
                try {
                    String uploadDir = "uploads/";
                    File uploadFile = new File(uploadDir + file.getOriginalFilename());
                    file.transferTo(uploadFile);
                    return file.getOriginalFilename();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());
            announcement.setAttachments(filenames);
        }
        return announcementRepository.save(announcement);
    }

    public boolean deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) return false;
        announcementRepository.deleteById(id);
        return true;
    }
}
