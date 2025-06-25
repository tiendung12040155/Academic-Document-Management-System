package com.example.ADMS.repository;

import com.example.ADMS.entity.UserResource;
import com.example.ADMS.entity.type.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserResourceRepository extends JpaRepository<UserResource, Long> {
    @Modifying
    @Transactional
    @Query("delete from UserResource u where u.user.id = :id and u.resource.id = :resourceId and u.actionType = :actionType")
    void deleteUserResourceHasActionType(Long id, Long resourceId, ActionType actionType);

    @Query("select u from UserResource u where u.user.id = :id and u.resource.id = :resourceId and u.actionType = :actionType")
    Optional<UserResource> findUserResourceHasActionType(Long id, Long resourceId, ActionType actionType);

    @Query("select count(ur) from UserResource ur where ur.actionType = :actionType and ur.resource.id = :resourceId")
    Long countByActionTypeWithResource(ActionType actionType, Long resourceId);

    @Query("select count(ur) from UserResource ur where ur.user.id = :userId and ur.resource.approveType = 'ACCEPTED' " +
            " and ur.resource.visualType = 'PUBLIC' and ur.actionType = :actionType and ur.active = true and ur.resource.active = true")
    Long countUserResourceByType(Long userId, ActionType actionType);

    @Query("select count(ur) from Resource r join r.userResourceList ur where r.author.id = :authorId and r.active = true and ur.active = true")
    Long countUserLikeMyResource(Long authorId);

    @Query("select count(r) from Resource r where r.author.id = :userId and r.active = true")
    Long countResourceUploadedByUser(Long userId);
}
