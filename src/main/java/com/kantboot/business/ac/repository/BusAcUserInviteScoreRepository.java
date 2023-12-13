package com.kantboot.business.ac.repository;

import com.kantboot.business.ac.domain.entity.BusAcUserInviteScore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BusAcUserInviteScoreRepository extends JpaRepository<BusAcUserInviteScore,Long> {


    /**
     * 增加积分
     * @param userId
     * @param score
     */
    @Modifying
    @Transactional
    @Query(
            """
            update BusAcUserInviteScore set score = score + :score where userId = :userId
            """
    )
    void addScore(@Param("userId") Long userId, @Param("score") Double score);

    /**
     * 根据用户id获取积分
     */
    BusAcUserInviteScore findByUserId(@Param("userId") Long userId);
}
