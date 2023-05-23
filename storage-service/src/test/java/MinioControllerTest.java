import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = com.ithirteeng.messengerapi.storage.StorageServerApplication.class)
public class MinioControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MinioFileService minioFileService;

    @Test
    public void testUploadFile() throws Exception {
        assertNotNull(mockMvc);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_file.txt",
                "text/plain",
                "Test Text".getBytes()
        );
        FileDataDto fileDataDto = new FileDataDto(
                UUID.randomUUID().toString(),
                "test_file.txt",
                "Test Text".getBytes().length
        );
        when(minioFileService.uploadFile(file.getBytes(), file.getOriginalFilename())).thenReturn(fileDataDto);

        mockMvc.perform(multipart("/api/file/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId").value(fileDataDto.getFileId()))
                .andExpect(jsonPath("$.fileName").value("test_file.txt"))
                .andExpect(jsonPath("$.fileSize").value("Test Text".getBytes().length));
    }

    @Test
    public void testDownloadBinaryFile() throws Exception {
        String fileId = UUID.randomUUID().toString();
        byte[] file = "Text Test".getBytes();

        when(minioFileService.downloadFile(fileId)).thenReturn(file);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/file/download/binary/{id}", fileId)
                        .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(file));
    }

    @Test
    public void testDownloadDefaultFile() throws Exception {
        String fileId = UUID.randomUUID().toString();
        byte[] file = "Text Test".getBytes();

        FileDataDto fileData = new FileDataDto(
                fileId,
                "test_text.txt",
                file.length
        );

        when(minioFileService.downloadFile(fileId)).thenReturn(file);
        when(minioFileService.getFileDataById(fileId)).thenReturn(fileData);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/file/download/{id}", fileId)
                        .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "filename=test_text.txt"))
                .andExpect(MockMvcResultMatchers.content().bytes(file));
    }

    @Test
    public void testDownloadWithNameFile() throws Exception {
        String fileId = UUID.randomUUID().toString();
        byte[] file = "Text Test".getBytes();

        when(minioFileService.downloadFile(fileId)).thenReturn(file);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/file/download/{id}/{file}", fileId, "filename.test")
                        .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "filename=filename.test"))
                .andExpect(MockMvcResultMatchers.content().bytes(file));
    }
}
