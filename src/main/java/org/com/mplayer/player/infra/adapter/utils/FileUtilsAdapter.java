package org.com.mplayer.player.infra.adapter.utils;

import org.apache.commons.io.FilenameUtils;
import org.com.mplayer.player.domain.ports.out.utils.FileUtilsPort;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileUtilsAdapter implements FileUtilsPort {
    @Override
    public Map<String, Object> readFileMetadata(MultipartFile multipartFile) {
        File file = null;

        try {
            file = convertMultipartFileToFile(multipartFile);

            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            HashMap<String, Object> metadataMap = new HashMap<>();

            if (audioFile.getAudioHeader() != null) {
                metadataMap.put("length", audioFile.getAudioHeader().getTrackLength());
            }

            if (tag != null) {
                metadataMap.put("title", tag.getFirst(FieldKey.TITLE));
                metadataMap.put("artist", tag.getFirst(FieldKey.ARTIST));
                metadataMap.put("album", tag.getFirst(FieldKey.ALBUM));
                metadataMap.put("genre", tag.getFirst(FieldKey.GENRE));
                metadataMap.put("year", tag.getFirst(FieldKey.YEAR));
                metadataMap.put("composer", tag.getFirst(FieldKey.COMPOSER));
                metadataMap.put("cover", audioFile.getTag().getArtworkList().isEmpty() ? null : audioFile.getTag().getArtworkList().get(0).getBinaryData());
                metadataMap.put("cover_content_type", audioFile.getTag().getArtworkList().isEmpty() ? null : audioFile.getTag().getArtworkList().get(0).getMimeType().split("/")[1]);
            }

            return metadataMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = null;

        try {
            if (multipartFile.getOriginalFilename() == null) {
                throw new RuntimeException("Original filename is null!");
            }

            file = new File(multipartFile.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            return file;
        } catch (Exception e) {
            if (file != null && file.exists()) {
                file.delete();
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContentType(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    @Override
    public String fileNameWithoutExtension(MultipartFile multipartFile) {
        return FilenameUtils.removeExtension(multipartFile.getOriginalFilename());
    }

    public String fileNameWithoutExtension(String fileName) {
        return FilenameUtils.removeExtension(fileName);
    }

    public String fileExtension(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    public String fileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }
}
