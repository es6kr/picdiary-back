package picdiary.diary.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public void deleteImage(String fileName)  {
        amazonS3.deleteObject(bucket, fileName);
    }

    public URL getUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName);
    }

    /**
     * @param prefix 접두사에 {userId}/{date} 붙여서 중복 방지
     * @param multipartFile multipart/form-data 포맷으로 업로드한 이미지 파일
     * @return 버킷명을 제외한 경로를 포함한 파일명
     * @throws IOException 업로드 실패
     */
    public String saveFile(String prefix, MultipartFile multipartFile) throws IOException {
        String fileName = prefix + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        return fileName;
    }
}
