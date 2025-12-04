package retoy.hotels_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Краткая информация об отеле")
public class HotelShortDto {

    @Schema(description = "ID отеля", example = "1")
    private Long id;

    @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Описание отеля", example = "Luxurious hotel in the city center")
    private String description;

    @Schema(description = "Адрес отеля", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
    private String address;

    @Schema(description = "Телефон отеля", example = "+375 17 309-80-00")
    private String phone;
}
