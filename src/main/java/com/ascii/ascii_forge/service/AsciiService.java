package com.ascii.ascii_forge.service;

import com.ascii.ascii_forge.entity.AsciiArt;
import com.ascii.ascii_forge.repository.AsciiArtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsciiService {

    private final AsciiArtRepository asciiArtRepository;

    @Transactional
    public String convertToAscii(MultipartFile file) throws IOException {
        BufferedImage img = ImageIO.read(file.getInputStream());
        if (img == null) return "이미지를 읽을 수 없습니다.";

        // 1. 리사이징 (폭 120px 고정)
        int width = 120;
        int height = (int) (img.getHeight() * (double) width / img.getWidth() * 0.5);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();

        // 2. 아스키 변환
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(resized.getRGB(j, i));
                double pixVal = (color.getRed() * 0.30) + (color.getGreen() * 0.59) + (color.getBlue() * 0.11);
                sb.append(colorToChar(pixVal));
            }
            sb.append("\n");
        }
        String asciiResult = sb.toString();

        // 3. 100개 유지 로직 (오래된 것 삭제)
        if (asciiArtRepository.count() >= 100) {
            AsciiArt oldest = asciiArtRepository.findFirstByOrderByCreatedAtAsc();
            if (oldest != null) asciiArtRepository.delete(oldest);
        }

        // 4. 저장
        asciiArtRepository.save(AsciiArt.builder()
                .fileName(file.getOriginalFilename())
                .content(asciiResult)
                .build());

        return asciiResult;
    }

    public List<AsciiArt> getRecentArts() {
        return asciiArtRepository.findTop10ByOrderByCreatedAtDesc();
    }

    private char colorToChar(double pixval) {
        if (pixval >= 240) return ' ';
        else if (pixval >= 210) return '.';
        else if (pixval >= 190) return '*';
        else if (pixval >= 170) return '+';
        else if (pixval >= 120) return '^';
        else if (pixval >= 110) return '$';
        else if (pixval >= 80) return '4';
        else return '#'; // 가장 어두운 영역
    }
}