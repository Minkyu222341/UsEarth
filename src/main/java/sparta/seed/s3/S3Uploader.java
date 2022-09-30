package sparta.seed.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public S3Dto upload(MultipartFile multipartFile) throws IOException {

    String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
    String fileFormatName = multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/") + 1);
    MultipartFile resize = resize(fileName, fileFormatName, multipartFile);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(resize.getSize());
    objectMetadata.setContentType(resize.getContentType());
    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resize.getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
    String result = amazonS3Client.getUrl(bucket, fileName).toString();
//    removeNewFile(new File(Objects.requireNonNull(resize.getOriginalFilename())));
    return new S3Dto(fileName, result);
  }

  private MultipartFile resize(String fileName, String fileFormatName, MultipartFile originalImage) throws IOException {

    // 요청 받은 파일로 부터 BufferedImage 객체를 생성합니다.
    BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

    int demandWidth = 1920;

    // 원본 이미지의 너비와 높이 입니다.
    int originWidth = srcImg.getWidth();
    int originHeight = srcImg.getHeight();

    // 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산합니다.
    int newWidth;
    int newHeight;

    // 원본 넓이가 더 작을경우 리사이징 안함.
    if (demandWidth >= originWidth) {
      newWidth = originWidth;
      newHeight = originHeight;
    } else {
      newWidth = demandWidth;
      newHeight = (demandWidth * originHeight) / originWidth;
    }

    BufferedImage destImg = Scalr.resize(srcImg, newWidth, newHeight);


    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(destImg, fileFormatName.toLowerCase(), byteArrayOutputStream);
    byteArrayOutputStream.flush();
    destImg.flush();
    return new MockMultipartFile(fileName, byteArrayOutputStream.toByteArray());
  }

  private void removeNewFile(File targetFile) {
    if (targetFile.delete()) {
      log.info("파일이 삭제되었습니다.");
    } else {
      log.info("파일이 삭제되지 못했습니다.");
    }
  }
}