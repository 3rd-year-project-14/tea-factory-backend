package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.AnnouncementDTO;
import com.teafactory.pureleaf.entity.Announcement;
import com.teafactory.pureleaf.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> addAnnouncement(
            @RequestParam("topic") String topic,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("factories") List<String> factories,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        // Validate required fields
        if (topic == null || topic.isEmpty() || subject == null || subject.isEmpty() || content == null || content.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing required fields: topic, subject, or content");
        }

        // Create DTO from request parameters
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        announcementDTO.setTopic(topic);
        announcementDTO.setSubject(subject);
        announcementDTO.setContent(content);
        announcementDTO.setFactories(factories.stream().map(Long::valueOf).collect(Collectors.toList()));

        // Pass data to the service layer
        Announcement announcement = announcementService.addAnnouncement(announcementDTO, attachments);
        return ResponseEntity.ok(announcement);
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        if (announcement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(announcement);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @RequestParam("topic") String topic,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("factories") List<String> factories,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        if (topic == null || topic.isEmpty() || subject == null || subject.isEmpty() || content == null || content.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing required fields: topic, subject, or content");
        }
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        announcementDTO.setTopic(topic);
        announcementDTO.setSubject(subject);
        announcementDTO.setContent(content);
        announcementDTO.setFactories(factories.stream().map(Long::valueOf).collect(Collectors.toList()));
        Announcement updated = announcementService.updateAnnouncement(id, announcementDTO, attachments);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        boolean deleted = announcementService.deleteAnnouncement(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
