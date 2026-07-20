package backend.report.response;

import backend.common.enums.UserRole;
import backend.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * Qaybta users-ka ee complete system report.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportSection {

    /*
     * Tirada guud ee users-ka.
     */
    private long totalUsers;

    /*
     * Tirada ACTIVE users-ka.
     */
    private long activeUsers;

    /*
     * Tirada INACTIVE users-ka.
     */
    private long inactiveUsers;

    /*
     * Tirada BLOCKED users-ka.
     */
    private long blockedUsers;

    /*
     * Tirada ADMIN users-ka.
     */
    private long totalAdmins;

    /*
     * Tirada STAFF users-ka.
     */
    private long totalStaff;

    /*
     * Tirada regular users-ka.
     */
    private long totalRegularUsers;

    /*
     * Liiska dhammaan users-ka.
     */
    private List<UserItem> records;

    /*
     * Hal row oo user report-ka ka mid ah.
     *
     * Password-ka halkan laguma soo bandhigayo.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItem {

        private Long id;

        private String fullName;

        private String email;

        private UserRole role;

        private UserStatus status;
    }
}