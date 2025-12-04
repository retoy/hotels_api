package retoy.hotels_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import retoy.hotels_api.model.Address;
import retoy.hotels_api.model.Contacts;
import retoy.hotels_api.model.ArrivalTime;
import java.util.Set;

@Data
@Schema(description = "Подробная информация об отеле")
public class HotelDetailDto {
    @Schema(description = "ID отеля", example = "1")
    private Long id;

    @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Бренд", example = "Hilton")
    private String brand;

    @Schema(description = "Описание отеля")
    private String description;

    @Schema(description = "Адрес отеля")
    private Address address;

    @Schema(description = "Контактная информация")
    private Contacts contacts;

    @Schema(description = "Время заезда/выезда")
    private ArrivalTime arrivalTime;

    @Schema(description = "Список удобств",
            example = "[\"Free WiFi\", \"Parking\", \"Swimming Pool\"]")
    private Set<String> amenities;
}