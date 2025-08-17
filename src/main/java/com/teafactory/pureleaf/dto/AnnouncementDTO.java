package com.teafactory.pureleaf.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementDTO {

    private String topic;
    private String subject;
    private String content;
    private List<Long> factories;               // factory IDs as numbers
    private List<MultipartFile> attachments;    // actual uploaded files

    // Getters and Setters
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Long> getFactories() { return factories; }
    public void setFactories(List<Long> factories) { this.factories = factories; }

    public List<MultipartFile> getAttachments() { return attachments; }
    public void setAttachments(List<MultipartFile> attachments) { this.attachments = attachments; }
}
