package com.runwithme.runwithme.global.utils;

import com.runwithme.runwithme.global.error.CustomException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.runwithme.runwithme.global.result.ResultCode.FAILED_CONVERT;

@Slf4j
@Getter
public class MultipartFileUtils {

    private final MultipartFile multipartFile;

    private final String originalFileName;

    private final String uuidFileName;

    public MultipartFileUtils(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;

        this.originalFileName = multipartFile.getOriginalFilename();

        this.uuidFileName = makeUuidFileName(multipartFile);
    }

    public Optional<File> convertToFile() {
        File convertFile = new File(uuidFileName);
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (Exception e) {
            throw new CustomException(FAILED_CONVERT);
        }
        return Optional.empty();
    }

    public String getFileType() {

        String contentType = multipartFile.getContentType();

        int index = contentType.indexOf("/");

        return contentType.substring(0, index);
    }

    private static String makeUuidFileName(MultipartFile file) {
        // 랜덤 UUID값을 서버에 저장한다.
        String uuid = String.valueOf(UUID.randomUUID());

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        return uuid + "." + extension;
    }
}