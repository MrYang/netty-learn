package com.zz.nettystudy.push.server.repository;

import com.zz.nettystudy.push.common.entity.ServerMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<ServerMessage, String>, JpaSpecificationExecutor<ServerMessage> {

    List<ServerMessage> findByDeviceId(String deviceId);
}
