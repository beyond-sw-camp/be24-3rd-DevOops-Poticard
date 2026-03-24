package org.example.porti.chat.message;

import org.example.porti.chat.message.model.ChatMessage;
import org.example.porti.chat.message.model.ChatMessageDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findAllByChatRoomIdxOrderByCreatedAtDesc(Long roomIdx, Pageable pageable);

    @Query("SELECT m.chatRoom.idx as roomIdx, COUNT(m) as count " +
            "FROM ChatMessage m " +
            "WHERE m.chatRoom.idx IN :roomIds " +
            "AND m.user.idx != :userIdx " +
            "AND m.isRead = false " +
            "GROUP BY m.chatRoom.idx")
    List<ChatMessageDto.UnreadCount> countUnreadByRoomIds(@Param("roomIds") List<Long> roomIds, @Param("userIdx") Long userIdx);

    Optional<ChatMessage> findFirstByChatRoomIdxOrderByCreatedAtDesc(Long roomIdx);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.chatRoom.idx = :roomIdx AND m.user.idx != :userIdx AND m.isRead = false")
    void markAsReadByRoomIdxAndNotUserIdx(@Param("roomIdx") Long roomIdx, @Param("userIdx") Long userIdx);
}
