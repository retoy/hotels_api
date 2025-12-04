package retoy.hotels_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retoy.hotels_api.dto.HotelToCreateDto;
import retoy.hotels_api.dto.HotelShortDto;
import retoy.hotels_api.dto.HotelDetailDto;
import retoy.hotels_api.mapper.HotelMapper;
import retoy.hotels_api.model.Hotel;
import retoy.hotels_api.service.HotelService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotels", description = "API для работы с отелями")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/hotels")
    @Operation(
            summary = "Получить список всех отелей",
            description = "Возвращает список отелей с краткой информацией"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelShortDto.class))
    )
    public List<HotelShortDto> getAllHotels() {
        return hotelService.getAllHotels().stream()
                .map(hotelMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Получить отель по ID")
    @ApiResponse(responseCode = "200", description = "Отель найден")
    @ApiResponse(responseCode = "404", description = "Отель не найден")
    public ResponseEntity<HotelDetailDto> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        HotelDetailDto dto = hotelMapper.toDetailDto(hotel);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск отелей")
    public List<HotelShortDto> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {

        return hotelService.searchHotels(name, brand, city, country, amenities).stream()
                .map(hotelMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Гистограмма отелей по параметру")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }

    @PostMapping("/hotels")
    @Operation(summary = "Создать новый отель")
    @ApiResponse(responseCode = "201", description = "Отель создан")
    @ApiResponse(responseCode = "400", description = "Неверные данные")
    public ResponseEntity<HotelShortDto> createHotel(
            @Valid @RequestBody HotelToCreateDto requestDTO) {
        Hotel hotel = hotelMapper.toEntity(requestDTO);
        Hotel savedHotel = hotelService.createHotel(hotel);
        HotelShortDto responseDto = hotelMapper.toShortDto(savedHotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Добавить удобства к отелю")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @PathVariable Long id,
            @RequestBody Set<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.ok().build();
    }
}
