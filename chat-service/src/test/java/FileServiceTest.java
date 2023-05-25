import com.ithirteeng.messengerapi.chat.entity.FileEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import com.ithirteeng.messengerapi.chat.mapper.FileMapper;
import com.ithirteeng.messengerapi.chat.repository.FileRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.chat.service.CommonService;
import com.ithirteeng.messengerapi.chat.service.FileService;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.FileDataDto;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private FileService fileService;

    @Test
    public void testAttachFilesToMessage() {
        List<UUID> list = Lists.list(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        FileDataDto fileDataDto = new FileDataDto("id", "name", 123);

        when(messageRepository.findById(any(UUID.class))).thenReturn(Optional.of(new MessageEntity()));
        when(commonService.getFileData(any(String.class))).thenReturn(fileDataDto);

       try {
           mockStatic(FileMapper.class);
       } catch (Exception e){}
        when(FileMapper.createNewFile(any(FileDataDto.class), any(MessageEntity.class))).thenReturn(new FileEntity());

        fileService.attachFilesToMessage(list, UUID.randomUUID());
    }

    @Test(expected = NotFoundException.class)
    public void testAttachFilesToMessage_ThrowsNotFoundException() {
        List<UUID> list = Lists.list(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        FileDataDto fileDataDto = new FileDataDto("id", "name", 123);

        when(messageRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(commonService.getFileData(any(String.class))).thenReturn(fileDataDto);

        try {
            mockStatic(FileMapper.class);
        } catch (Exception e){}
        when(FileMapper.createNewFile(any(FileDataDto.class), any(MessageEntity.class))).thenReturn(new FileEntity());

        fileService.attachFilesToMessage(list, UUID.randomUUID());
    }

}
