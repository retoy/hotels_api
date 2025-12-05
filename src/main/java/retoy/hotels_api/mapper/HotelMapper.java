package retoy.hotels_api.mapper;

import org.springframework.stereotype.Component;
import retoy.hotels_api.dto.HotelToCreateDto;
import retoy.hotels_api.dto.HotelDetailDto;
import retoy.hotels_api.model.Hotel;
import retoy.hotels_api.dto.HotelShortDto;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelToCreateDto dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setBrand(dto.getBrand());
        hotel.setDescription(dto.getDescription());
        hotel.setAddress(dto.getAddress());
        hotel.setContacts(dto.getContacts());
        hotel.setArrivalTime(dto.getArrivalTime());
        return hotel;
    }
    public HotelShortDto toShortDto(Hotel hotel) {
        HotelShortDto dto = new HotelShortDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setAddress(formatAddress(hotel));
        dto.setPhone(extractPhone(hotel));
        return dto;
    }

    public HotelDetailDto toDetailDto(Hotel hotel) {
        HotelDetailDto dto = new HotelDetailDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setBrand(hotel.getBrand());
        dto.setDescription(hotel.getDescription());
        dto.setAddress(hotel.getAddress());
        dto.setContacts(hotel.getContacts());
        dto.setArrivalTime(hotel.getArrivalTime());
        dto.setAmenities(hotel.getAmenities());
        return dto;
    }

    private String formatAddress(Hotel hotel) {
        if (hotel.getAddress() == null) return null;
        return String.format("%s %s, %s, %s, %s",
                hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(),
                hotel.getAddress().getCity(),
                hotel.getAddress().getPostCode(),
                hotel.getAddress().getCountry());
    }

    private String extractPhone(Hotel hotel) {
        return hotel.getContacts() != null ? hotel.getContacts().getPhone() : null;
    }
}
