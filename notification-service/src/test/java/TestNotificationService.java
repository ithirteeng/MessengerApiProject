import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.notification.dto.*;
import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import com.ithirteeng.messengerapi.notification.repository.NotificationRepository;
import com.ithirteeng.messengerapi.notification.service.NotificationService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TestNotificationService {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testCountUnreadNotifications() {
        var id = UUID.randomUUID();
        when(notificationRepository.countByUserIdAndStatus(id, NotificationStatus.NOT_READ)).thenReturn(10);
        var result = notificationService.countNotReadNotifications(id);

        assertNotNull(result);
        assertEquals(10, result);
    }

    @Test
    public void testSetStatusToNotifications() {
        List<UUID> notificationsList = Collections.singletonList(UUID.randomUUID());
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(notificationsList, NotificationStatus.READ);

        when(notificationRepository.existsAllByIdAndUserId(any(UUID.class), any(UUID.class))).thenReturn(true);

        when(notificationRepository.countByUserIdAndStatus(any(UUID.class), any(NotificationStatus.class))).thenReturn(10);

        var result = notificationService.setStatusToNotifications(updateStatusDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(10, result);
    }

    @Test
    public void testGetNotifications() {
        PageInfoDto pageInfoDto = new PageInfoDto();
        pageInfoDto.setPageNumber(1);
        pageInfoDto.setPageSize(10);
        DatePeriodDto datePeriodDto = new DatePeriodDto(LocalDateTime.now(), LocalDateTime.now());
        List<NotificationType> notificationTypes = Lists.newArrayList(NotificationType.LOGIN, NotificationType.MESSAGE);
        NotificationsFiltersDto notificationsFiltersDto = new NotificationsFiltersDto(
                datePeriodDto,
                "test",
                notificationTypes
        );

        PageFiltersDto pageFiltersDto = new PageFiltersDto(pageInfoDto, notificationsFiltersDto);
        Page<NotificationEntity> page = mock(Page.class);
        when(notificationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        var result = notificationService.getNotifications(pageFiltersDto, UUID.randomUUID());

        assertNotNull(result);
        assertEquals(pageInfoDto.getPageSize(), result.getPageInfo().getPageSize());
        assertEquals(pageInfoDto.getPageNumber(), result.getPageInfo().getPageNumber());
        assertEquals(page.getTotalPages(), result.getPageInfo().getPagesCount());
    }

    @Test(expected = BadRequestException.class)
    public void testGetNotifications_ThrowsBadRequestException() {
        PageInfoDto pageInfoDto = new PageInfoDto();
        pageInfoDto.setPageNumber(0);
        pageInfoDto.setPageSize(10);
        DatePeriodDto datePeriodDto = new DatePeriodDto(LocalDateTime.now(), LocalDateTime.now());
        List<NotificationType> notificationTypes = Lists.newArrayList(NotificationType.LOGIN, NotificationType.MESSAGE);
        NotificationsFiltersDto notificationsFiltersDto = new NotificationsFiltersDto(
                datePeriodDto,
                "test",
                notificationTypes
        );

        PageFiltersDto pageFiltersDto = new PageFiltersDto(pageInfoDto, notificationsFiltersDto);
        Page<NotificationEntity> page = mock(Page.class);
        when(notificationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        var result = notificationService.getNotifications(pageFiltersDto, UUID.randomUUID());
  }

}
