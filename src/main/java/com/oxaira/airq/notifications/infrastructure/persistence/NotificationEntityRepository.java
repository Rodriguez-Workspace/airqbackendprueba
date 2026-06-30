package com.oxaira.airq.notifications.infrastructure.persistence;

import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.notifications.domain.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByClientOrderByCreatedAtDesc(User client);
    NotificationEntity findFirstByClientAndLocationOrderByCreatedAtDesc(User client, String location);
    NotificationEntity findFirstByClientAndLocationAndTypeOrderByCreatedAtDesc(User client, String location, String type);
}
