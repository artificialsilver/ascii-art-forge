package com.ascii.ascii_forge.repository;

import com.ascii.ascii_forge.entity.AsciiArt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsciiArtRepository extends JpaRepository<AsciiArt, Long> {
    // 최근 10개 조회 (목록용)
    List<AsciiArt> findTop10ByOrderByCreatedAtDesc();

    // 가장 오래된 데이터 1개 찾기 (100개 제한 삭제용)
    AsciiArt findFirstByOrderByCreatedAtAsc();
}