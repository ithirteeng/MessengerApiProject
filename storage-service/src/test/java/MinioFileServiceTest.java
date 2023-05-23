import com.ithirteeng.messengerapi.common.exception.FileException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.entity.FilesEntity;
import com.ithirteeng.messengerapi.storage.repository.FilesRepository;
import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import com.ithirteeng.messengerapi.storage.utils.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MinioFileServiceTest {
    @Mock
    private FilesRepository filesRepository;

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioConfig minioConfig;

    @InjectMocks
    private MinioFileService minioFileService;

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_file.txt",
                "text/plain",
                "Test Text".getBytes()
        );

        String testBucketName = "test-bucket";
        when(minioConfig.getBucket()).thenReturn(testBucketName);

        FileDataDto fileData = minioFileService.uploadFile(file.getBytes(), file.getOriginalFilename());

        assertNotNull(fileData);
        assertEquals("test_file.txt", fileData.getFileName());
        assertEquals(file.getBytes().length, fileData.getFileSize());
    }

    @Test
    public void testDownload() throws Exception {
        UUID fileId = UUID.randomUUID();
        String testBucketName = "test-bucket";
        byte[] testFile = "Test Text".getBytes();

        when(minioConfig.getBucket()).thenReturn(testBucketName);

        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        when(getObjectResponse.readAllBytes()).thenReturn(testFile);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(getObjectResponse);


        byte[] fileData = minioFileService.downloadFile(fileId.toString());

        assertNotNull(fileData);
        assertEquals(fileData, testFile);
    }

    @Test
    public void testDownload_ThrowsFileException() {
        String fileId = UUID.randomUUID().toString();
        String testBucketName = "test-bucket";
        when(minioConfig.getBucket()).thenReturn(testBucketName);
        assertThrows(FileException.class, () -> minioFileService.downloadFile(fileId));
    }

    @Test
    public void testFileExistingCheckMethod_FileExists() {
        UUID fileId = UUID.randomUUID();
        when(filesRepository.existsById(fileId.toString())).thenReturn(true);
        assertTrue(minioFileService.checkIfFileExists(fileId.toString()));
    }

    @Test
    public void testFileExistingCheckMethod_FileNotExists() {
        UUID fileId = UUID.randomUUID();
        when(filesRepository.existsById(fileId.toString())).thenReturn(false);
        assertFalse(minioFileService.checkIfFileExists(fileId.toString()));
    }

    @Test
    public void testGetFileDataById_FileExists() {
        UUID fileId = UUID.randomUUID();
        String testFileName = "test_file.txt";
        byte[] testFile = "Test Text".getBytes();

        FilesEntity filesEntity = new FilesEntity(
                fileId.toString(),
                testFileName,
                testFile.length
        );

        when(filesRepository.findById(fileId.toString())).thenReturn(Optional.of(filesEntity));

        FileDataDto fileData = new FileDataDto(
                fileId.toString(),
                testFileName,
                testFile.length
        );

        assertEquals(fileData, minioFileService.getFileDataById(fileId.toString()));
    }

    @Test(expected = NotFoundException.class)
    public void testGetFileDataById_FileNotExists() {
        UUID fileId = UUID.randomUUID();
        when(filesRepository.findById(fileId.toString())).thenReturn(Optional.empty());
        minioFileService.getFileDataById(fileId.toString());
    }


}
