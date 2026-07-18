package backend.announcement.service;


import backend.announcement.Model.Announcement;
import backend.announcement.repository.AnnouncementRepository;
import backend.announcement.request.AnnouncementUpdateRequest;
import backend.announcement.request.CreateAnnouncementRequest;
import backend.announcement.response.AnnouncementResponse;
import backend.category.entity.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.AnnouncementStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AnnouncementService {


    private final AnnouncementRepository announcementRepository;
    private final CategoryRepository categoryRepository;



    // Constructor Injection
    public AnnouncementService(
            AnnouncementRepository announcementRepository,
            CategoryRepository categoryRepository
    ) {
        this.announcementRepository = announcementRepository;
        this.categoryRepository = categoryRepository;
    }





    // CREATE ANNOUNCEMENT
    @Transactional
    public AnnouncementResponse createAnnouncement(
            CreateAnnouncementRequest request
    ) {


        BloodCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Blood category not found")
                );



        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .category(category)
                .quantityNeeded(request.getQuantityNeeded())
                .urgency(request.getUrgency())
                .location(request.getLocation())
                .description(request.getDescription())
                .status(AnnouncementStatus.ACTIVE)
                .build();


        Announcement savedAnnouncement =
                announcementRepository.save(announcement);


        return mapToResponse(savedAnnouncement);
    }





    // GET ALL ANNOUNCEMENTS
    public List<AnnouncementResponse> getAllAnnouncements() {


        return announcementRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }





    // GET ANNOUNCEMENT BY ID
    public AnnouncementResponse getAnnouncementById(Long id) {


        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Announcement not found")
                        );


        return mapToResponse(announcement);
    }

    // GET ACTIVE ANNOUNCEMENTS
    public List<AnnouncementResponse> getActiveAnnouncements() {


        return announcementRepository
                .findByStatus(AnnouncementStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }





    // UPDATE ANNOUNCEMENT
    @Transactional
    public AnnouncementResponse updateAnnouncement(
            Long id,
            AnnouncementUpdateRequest request
    ) {


        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Announcement not found")
                        );


        announcement.setTitle(request.getTitle());

        announcement.setQuantityNeeded(
                request.getQuantityNeeded()
        );

        announcement.setUrgency(
                request.getUrgency()
        );

        announcement.setLocation(
                request.getLocation()
        );

        announcement.setDescription(
                request.getDescription()
        );


        Announcement updatedAnnouncement =
                announcementRepository.save(announcement);


        return mapToResponse(updatedAnnouncement);
    }





    // DELETE ANNOUNCEMENT
    @Transactional
    public void deleteAnnouncement(Long id) {


        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Announcement not found")
                        );


        announcementRepository.delete(announcement);
    }


    // CONVERT ENTITY TO RESPONSE
    private AnnouncementResponse mapToResponse(
            Announcement announcement
    ) {


        return new AnnouncementResponse(

                announcement.getId(),

                announcement.getTitle(),

                announcement.getCategory()
                        .getBloodGroup(),

                announcement.getQuantityNeeded(),

                announcement.getUrgency(),

                announcement.getLocation(),

                announcement.getDescription(),

                announcement.getStatus(),

                announcement.getCreatedAt()
        );
    }

}