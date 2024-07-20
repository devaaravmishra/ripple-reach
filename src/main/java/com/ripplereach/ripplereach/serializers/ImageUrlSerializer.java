package com.ripplereach.ripplereach.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ripplereach.ripplereach.services.ImageService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImageUrlSerializer extends JsonSerializer<String> {

  private ImageService imageService;

  @Override
  public void serialize(
      String imagePath, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (imagePath == null) {
      jsonGenerator.writeNull();
    } else {
      String signedUrl = imageService.generateSignedUrl(imagePath);
      jsonGenerator.writeString(signedUrl);
    }
  }
}
