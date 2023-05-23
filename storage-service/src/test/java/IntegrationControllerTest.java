import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.storage.service.MinioFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = com.ithirteeng.messengerapi.storage.StorageServerApplication.class)
public class IntegrationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MinioFileService minioFileService;

    @Test
    public void testCheckFileExisting() throws Exception {
        String fileId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.get("/integration/file/check/{id}", fileId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFileDataId() throws Exception {
        String fileId = UUID.randomUUID().toString();
        FileDataDto fileDataDto = new FileDataDto(
                fileId,
                "test.txt",
                "Hi, man".getBytes().length
        );
        when(minioFileService.getFileDataById(fileId)).thenReturn(fileDataDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/integration/file/{id}", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId").value(fileId))
                .andExpect(jsonPath("$.fileName").value("test.txt"))
                .andExpect(jsonPath("$.fileSize").value("Hi, man".getBytes().length));
    }

}
