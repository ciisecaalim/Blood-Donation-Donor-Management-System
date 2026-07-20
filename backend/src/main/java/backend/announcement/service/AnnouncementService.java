package backend.announcement.service;

import backend.announcement.Model.Announcement;
import backend.announcement.repository.AnnouncementRepository;
import backend.announcement.request.AnnouncementStatusRequest;
import backend.announcement.request.AnnouncementUpdateRequest;
import backend.announcement.request.CreateAnnouncementRequest;
import backend.announcement.response.AnnouncementResponse;
import backend.category.Model.BloodCategory;
import backend.category.repository.CategoryRepository;
import backend.common.enums.AnnouncementStatus;
import backend.common.enums.CategoryStatus;
import backend.exception.BadRequestException;
import backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    private final CategoryRepository categoryRepository;

    /*
     * Samee announcement cusub.
     */
    @Transactional
    public AnnouncementResponse createAnnouncement(
            CreateAnnouncementRequest request
    ) {

        // Hubi in blood category-gu database-ka ku jiro.
        BloodCategory category =
                categoryRepository.findById(
                                request.getCategoryId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood category not found"
                                )
                        );

        // Announcement waxaa loo samayn karaa
        // category ACTIVE ah oo keliya.
        if (category.getStatus() != CategoryStatus.ACTIVE) {
            throw new BadRequestException(
                    "Blood category must be ACTIVE"
            );
        }

        // Samee entity cusub.
        Announcement announcement =
                Announcement.builder()
                        .title(request.getTitle().trim())
                        .category(category)
                        .quantityNeeded(
                                request.getQuantityNeeded()
                        )
                        .urgency(request.getUrgency())
                        .location(request.getLocation().trim())
                        .description(
                                normalizeDescription(
                                        request.getDescription()
                                )
                        )
                        .status(AnnouncementStatus.ACTIVE)
                        .build();

        // Database-ka ku kaydi.
        Announcement savedAnnouncement =
                announcementRepository.save(announcement);

        // Entity-ga u beddel response DTO.
        return mapToResponse(savedAnnouncement);
    }

    /*
     * Soo qaad dhammaan announcements-ka.
     */
    @Transactional(readOnly = true)
    public List<AnnouncementResponse>
    getAllAnnouncements() {

        return announcementRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * Soo qaad hal announcement.
     */
    @Transactional(readOnly = true)
    public AnnouncementResponse getAnnouncementById(
            Long id
    ) {

        Announcement announcement =
                findAnnouncementEntityById(id);

        return mapToResponse(announcement);
    }

    /*
     * Soo qaad announcements-ka ACTIVE ah oo keliya.
     */
    @Transactional(readOnly = true)
    public List<AnnouncementResponse>
    getActiveAnnouncements() {

        return announcementRepository
                .findByStatus(
                        AnnouncementStatus.ACTIVE
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * Wax ka beddel announcement content-kiisa.
     *
     * Status-ka method-kan laguma beddelayo.
     */
    @Transactional
    public AnnouncementResponse updateAnnouncement(
            Long id,
            AnnouncementUpdateRequest request
    ) {

        Announcement announcement =
                findAnnouncementEntityById(id);

        announcement.setTitle(
                request.getTitle().trim()
        );

        announcement.setQuantityNeeded(
                request.getQuantityNeeded()
        );

        announcement.setUrgency(
                request.getUrgency()
        );

        announcement.setLocation(
                request.getLocation().trim()
        );

        announcement.setDescription(
                normalizeDescription(
                        request.getDescription()
                )
        );

        Announcement updatedAnnouncement =
                announcementRepository.save(announcement);

        return mapToResponse(updatedAnnouncement);
    }

    /*
     * Beddel status-ka announcement-ka.
     */
    @Transactional
    public AnnouncementResponse updateAnnouncementStatus(
            Long id,
            AnnouncementStatusRequest request
    ) {

        Announcement announcement =
                findAnnouncementEntityById(id);

        AnnouncementStatus newStatus =
                request.getStatus();

        // Haddii status-ku isla kii hore yahay,
        // update looma baahna.
        if (announcement.getStatus() == newStatus) {
            throw new BadRequestException(
                    "Announcement already has status: "
                            + newStatus
            );
        }

        announcement.setStatus(newStatus);

        Announcement updatedAnnouncement =
                announcementRepository.save(announcement);

        return mapToResponse(updatedAnnouncement);
    }

    /*
     * Tirtir announcement.
     *
     * Mashruuc dhab ah waxaa ka fiican in status-ka
     * CANCELLED laga dhigo halkii la tirtiri lahaa.
     */
    @Transactional
    public void deleteAnnouncement(Long id) {

        Announcement announcement =
                findAnnouncementEntityById(id);

        announcementRepository.delete(announcement);
    }

    /*
     * Soo qaad entity-ga announcement-ka.
     *
     * Method-kan waxaa dib u isticmaalaya
     * get, update, status update iyo delete.
     */
    private Announcement findAnnouncementEntityById(
            Long id
    ) {

        return announcementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Announcement not found with id: "
                                        + id
                        )
                );
    }

    /*
     * Entity-ga u beddel response DTO.
     */
    private AnnouncementResponse mapToResponse(
            Announcement announcement
    ) {

        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .bloodGroup(
                        announcement.getCategory()
                                .getBloodGroup()
                )
                .quantityNeeded(
                        announcement.getQuantityNeeded()
                )
                .urgency(announcement.getUrgency())
                .location(announcement.getLocation())
                .description(
                        announcement.getDescription()
                )
                .status(announcement.getStatus())
                .createdAt(announcement.getCreatedAt())
                .build();
    }

    /*
     * Description-ka nadiifi.
     */
    private String normalizeDescription(
            String description
    ) {

        if (description == null ||
                description.isBlank()) {
            return null;
        }

        return description
                .trim()
                .replaceAll("\\s+", " ");
    }
}