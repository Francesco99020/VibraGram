package com.vibragram.backend.service;

import com.vibragram.backend.model.Media;
import com.vibragram.backend.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaCleanupService {
    @Autowired
    private final MediaRepository mediaRepository;
    private final Path mediaDir = Paths.get("uploads/media");
    private final Path pfpDir = Paths.get("uploads/profile-photos");

    public MediaCleanupService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    //TODO: CHANGE THE SCHEDULE TIME BACK TO ONCE A DAY BEFORE PUSHING TO PRODUCTION
    @Scheduled(cron = "*/10 * * * * *") //(cron = "0 0 0 * * *")
    public void cleanupUnusedMedia() throws IOException {
        System.out.printf("Media clean up service has started sweep at %s%n", LocalDateTime.now());
        // Scan media for files
        List<Path> mediaFiles = ScanFilesToList(mediaDir);
        // check for postId or non expired UploadSessionId
        for (Path p : mediaFiles){
            String mediaUrl = "/uploads/media/" + p.getFileName().toString(); //This would need to change when dealing with PFP or other segments of media storage
            //check if PostID exists
            Media media = mediaRepository.getMediaByMediaURL(mediaUrl);
            if(media == null){//media does not have a valid entry in database and must be removed from storage
                System.out.printf("File named %s attempted to be deleted. Status: %s%n", p.getFileName(), Files.deleteIfExists(p));
                continue;
            }

            if(media.getPostId() == 0 && mediaRepository.getUploadSessionByUUID(media.getUploadSessionId()).getExpiresAt().isBefore(LocalDateTime.now())){
                //media UUID is expired and does not belong to a post, must be deleted.
                System.out.printf("File named %s attempted to be deleted. Status: %s%n", p.getFileName(), Files.deleteIfExists(p));
                //remove media entry from database
                System.out.printf("Database Media entry #: %s deletion status: %s", media.getMediaId(), mediaRepository.deleteMedia(media.getMediaId()));
            }
        }
        System.out.printf("Media clean up service has finished sweep at %s%n", LocalDateTime.now());
    }

    public static List<Path> ScanFilesToList(Path path) throws IOException {
        List<Path> files = new ArrayList<>();

        if (Files.exists(path) && Files.isDirectory(path)) {
            try (var stream = Files.list(path)) {
                files = stream
                        .filter(Files::isRegularFile) // Filter for only regular files, exclude subdirectories
                        .collect(Collectors.toList());
            }
        }
        return files;
    }
}
