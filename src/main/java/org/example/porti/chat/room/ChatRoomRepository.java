package org.example.porti.chat.room;

import jakarta.persistence.LockModeType;
import org.example.porti.chat.room.model.ChatRoom;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @EntityGraph(attributePaths = {"hostUser", "guestUser"})
    Slice<ChatRoom> findAllByHostUserIdxOrGuestUserIdx(Long hostUserIdx, Long guestUserIdx, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ChatRoom r WHERE r.idx = :roomIdx")
    Optional<ChatRoom> findByIdWithPessimisticLock(@Param("roomIdx") Long roomIdx);

    ChatRoom findByHostUserIdxAndGuestUserIdx(Long hostUserIdx, Long guestUserIdx);
}
