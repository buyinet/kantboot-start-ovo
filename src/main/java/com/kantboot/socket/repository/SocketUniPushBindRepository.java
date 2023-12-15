package com.kantboot.socket.repository;

import com.kantboot.socket.domain.entity.SocketUniPushBind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocketUniPushBindRepository extends JpaRepository<SocketUniPushBind,Long> {

    SocketUniPushBind findByUserIdAndCid(Long userId,String cid);

    List<SocketUniPushBind> findByUserId(Long userId);

}
