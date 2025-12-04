package retoy.hotels_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retoy.hotels_api.exception.HotelNotFoundException;
import retoy.hotels_api.model.Hotel;
import retoy.hotels_api.repository.HotelRepository;
import retoy.hotels_api.specification.HotelSpecification;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public List<Hotel> searchHotels(String name, String brand, String city,
                                    String country, List<String> amenities) {

        Specification<Hotel> spec = Specification.where(HotelSpecification.hasName(name))
                .and(HotelSpecification.hasBrand(brand))
                .and(HotelSpecification.hasCity(city))
                .and(HotelSpecification.hasCountry(country))
                .and(HotelSpecification.hasAmenities(amenities));

        return hotelRepository.findAll(spec);
    }

    public Map<String, Long> getHistogram(String param) {
        return switch (param.toLowerCase()) {
            case "brand" -> getHistogramByBrand();
            case "city" -> getHistogramByCity();
            case "country" -> getHistogramByCountry();
            case "amenities" -> getHistogramByAmenities();
            default -> throw new IllegalArgumentException("Invalid parameter: " + param
                    + ". Valid parameters: brand, city, country, amenities");
        };
    }

    private Map<String, Long> getHistogramByBrand() {
        return hotelRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Hotel::getBrand,
                        Collectors.counting()
                ));
    }

    private Map<String, Long> getHistogramByCity() {
        return hotelRepository.findAll().stream()
                .filter(hotel -> hotel.getAddress() != null && hotel.getAddress().getCity() != null)
                .collect(Collectors.groupingBy(
                        hotel -> hotel.getAddress().getCity(),
                        Collectors.counting()
                ));
    }

    private Map<String, Long> getHistogramByCountry() {
        return hotelRepository.findAll().stream()
                .filter(hotel -> hotel.getAddress() != null && hotel.getAddress().getCountry() != null)
                .collect(Collectors.groupingBy(
                        hotel -> hotel.getAddress().getCountry(),
                        Collectors.counting()
                ));
    }

    private Map<String, Long> getHistogramByAmenities() {
        Map<String, Long> histogram = new HashMap<>();

        hotelRepository.findAll().forEach(hotel -> {
            if (hotel.getAmenities() != null) {
                hotel.getAmenities().forEach(amenity -> {
                    histogram.put(amenity, histogram.getOrDefault(amenity, 0L) + 1);
                });
            }
        });

        return histogram;
    }
    @Transactional
    public Hotel createHotel(Hotel hotel) {
        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new HashSet<>());
        }
        return hotelRepository.save(hotel);
    }

    @Transactional
    public void addAmenitiesToHotel(Long hotelId, Set<String> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            throw new IllegalArgumentException("Amenities list cannot be null or empty");
        }

        Hotel hotel = getHotelById(hotelId);

        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new HashSet<>());
        }

        hotel.getAmenities().addAll(new HashSet<>(amenities));
        hotelRepository.save(hotel);
    }
}
