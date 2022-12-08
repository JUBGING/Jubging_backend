package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.flask.FlaskResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FlaskApiService {

    public FlaskResponseDto requestToFlask(String fileName, MultipartFile file) throws Exception {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("image", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename() ));

        HttpHeaders uploadheaders = new HttpHeaders();
        uploadheaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<?> uploadEntity = new HttpEntity<>(params, uploadheaders);

        RestTemplate restTemplate = new RestTemplate();

        String requestUrl = "http://13.209.17.118:4000/hello";
        ResponseEntity<FlaskResponseDto> responseEntity = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                uploadEntity,
                FlaskResponseDto.class
        );
        return  responseEntity.getBody();
    }
    class MultipartInputStreamFileResource extends InputStreamResource {

        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // we do not want to generally read the whole stream into memory ...
        }
    }
}
