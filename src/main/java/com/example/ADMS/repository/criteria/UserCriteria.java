package com.example.ADMS.repository.criteria;

import com.example.ADMS.entity.User;
import com.example.ADMS.model.PagingDTOResponse;
import com.example.ADMS.model.dto.request.RoleDTODisplay;
import com.example.ADMS.model.dto.request.UserDTOFilter;
import com.example.ADMS.model.dto.response.UserDTOResponse;
import com.example.ADMS.model.mapper.RoleMapper;
import com.example.ADMS.model.mapper.UserMapper;
import com.example.ADMS.utils.S3Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCriteria {
    EntityManager em;
    S3Util s3Util;

    public PagingDTOResponse searchUser(UserDTOFilter userDTOFilter) {
        StringBuilder sql = new StringBuilder("select distinct u from User u join u.classObject c " +
                " join u.userRoleList ur join ur.role r where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (userDTOFilter.getActive() != null) {
            sql.append(" and u.active = :active ");
            params.put("active", userDTOFilter.getActive());
        }

        if (userDTOFilter.getName() != null) {
            sql.append(" and ( u.username like :username or u.email like :email ) ");
            params.put("username", "%" + userDTOFilter.getName() + "%");
            params.put("email", "%" + userDTOFilter.getName() + "%");
        }

        if (userDTOFilter.getRoleId() != null) {
            sql.append(" and r.id = :roleId");
            params.put("roleId", userDTOFilter.getRoleId());
        }

        if (userDTOFilter.getClassId() != null) {
            sql.append(" and c.id = :classId ");
            params.put("classId", userDTOFilter.getClassId());
        }

        sql.append(" order by u.createdAt desc ");

        Query countQuery = em.createQuery(sql.toString().replace("select distinct u", "select count(distinct u.id)"));

        Long pageIndex = userDTOFilter.getPageIndex();
        Long pageSize = userDTOFilter.getPageSize();

        TypedQuery<User> userTypedQuery = em.createQuery(sql.toString(), User.class);

        // Set param to query
        params.forEach((k, v) -> {
            userTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        userTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        userTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<User> userList = userTypedQuery.getResultList();

        Long totalUser = (Long) countQuery.getSingleResult();
        Long totalPage = totalUser / pageSize;
        if (totalUser % pageSize != 0) {
            totalPage++;
        }

        Set<UserDTOResponse> userDTOResponseList = userList.stream()
                .map(u -> {
                    UserDTOResponse response = UserMapper.toUserDTOResponse(u, s3Util);
                    List<RoleDTODisplay> roleDTOResponses = u.getUserRoleList().stream()
                            .filter(ur -> ur.getRole().getActive())
                            .map(role -> RoleMapper.toRoleDTODisplay(role.getRole()))
                            .toList();
                    return response;
                }).collect(Collectors.toSet());

        return PagingDTOResponse.builder()
                .totalElement(totalUser)
                .totalPage(totalPage)
                .data(userDTOResponseList)
                .build();
    }
}
