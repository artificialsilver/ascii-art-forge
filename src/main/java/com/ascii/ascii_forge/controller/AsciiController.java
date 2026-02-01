package com.ascii.ascii_forge.controller;

import com.ascii.ascii_forge.entity.AsciiArt;
import com.ascii.ascii_forge.service.AsciiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ascii")
@RequiredArgsConstructor
public class AsciiController {

    private final AsciiService asciiService;

    @PostMapping("/convert")
    public String convert(@RequestParam("file") MultipartFile file) throws IOException {
        return asciiService.convertToAscii(file);
    }

    @GetMapping("/recent")
    public List<AsciiArt> getRecent() {
        return asciiService.getRecentArts();
    }
}