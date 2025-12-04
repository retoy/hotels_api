package retoy.hotels_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import retoy.hotels_api.model.Address;
import retoy.hotels_api.model.ArrivalTime;
import retoy.hotels_api.model.Contacts;

@Data
@Schema(description = "Запрос на создание отеля")
public class HotelToCreateDto {

    @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
    @NotBlank(message = "Hotel name is required")
    private String name;

    @Schema(description = "Бренд", example = "Hilton")
    @NotBlank(message = "Brand is required")
    private String brand;

    @Schema(description = "Описание отеля")
    private String description;

    @Schema(description = "Адрес отеля")
    @Valid
    @NotNull(message = "Address is required")
    private Address address;

    @Schema(description = "Контактная информация")
    @Valid
    @NotNull(message = "Contacts are required")
    private Contacts contacts;

    @Schema(description = "Время заезда/выезда")
    @Valid
    private ArrivalTime arrivalTime;
}
