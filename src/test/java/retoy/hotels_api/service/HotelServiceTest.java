package retoy.hotels_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import retoy.hotels_api.exception.HotelNotFoundException;
import retoy.hotels_api.model.*;
import retoy.hotels_api.repository.HotelRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Hotel Service Unit Tests")
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel testHotel;
    private Address testAddress;
    @BeforeEach
    void setUp() {
         testAddress = new Address();
        testAddress.setHouseNumber(9);
        testAddress.setStreet("Pobediteley Avenue");
        testAddress.setCity("Minsk");
        testAddress.setCountry("Belarus");
        testAddress.setPostCode("220004");

        Contacts testContacts = new Contacts("+375 17 309-80-00", "info@hilton.com");
        ArrivalTime testArrivalTime = new ArrivalTime("14:00", "12:00");

        testHotel = new Hotel();
        testHotel.setId(1L);
        testHotel.setName("DoubleTree by Hilton Minsk");
        testHotel.setBrand("Hilton");
        testHotel.setDescription("Luxurious hotel");
        testHotel.setAddress(testAddress);
        testHotel.setContacts(testContacts);
        testHotel.setArrivalTime(testArrivalTime);
        testHotel.setAmenities(new HashSet<>(Arrays.asList("WiFi", "Parking")));
    }

    @Test
    @DisplayName("Should return hotel by ID when hotel exists")
    void getHotelById_WhenHotelExists_ReturnsHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));

        Hotel result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("DoubleTree by Hilton Minsk");

        verify(hotelRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when hotel not found by ID")
    void getHotelById_WhenHotelNotExists_ThrowsException() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(999L))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("Hotel not found");
    }

    @Test
    @DisplayName("Should return all hotels")
    void getAllHotels_ReturnsAllHotels() {
        List<Hotel> hotels = Arrays.asList(testHotel);
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testHotel);

        verify(hotelRepository).findAll();
    }

    @Test
    @DisplayName("Should search hotels with all parameters")
    void searchHotels_WithAllParameters_ReturnsFilteredHotels() {
        List<Hotel> expectedHotels = Arrays.asList(testHotel);
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(expectedHotels);

        List<Hotel> result = hotelService.searchHotels(
                "Hilton", "Hilton", "Minsk", "Belarus", Arrays.asList("WiFi")
        );

        assertThat(result).isEqualTo(expectedHotels);
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Should create hotel successfully")
    void createHotel_WithValidData_ReturnsSavedHotel() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        Hotel result = hotelService.createHotel(testHotel);

        assertThat(result).isEqualTo(testHotel);
        assertThat(result.getAmenities()).isNotEmpty();

        verify(hotelRepository).save(testHotel);
    }

    @Test
    @DisplayName("Should initialize empty amenities when creating hotel with null amenities")
    void createHotel_WithNullAmenities_InitializesEmptySet() {
        testHotel.setAmenities(null);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        Hotel result = hotelService.createHotel(testHotel);

        assertThat(result.getAmenities()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should add amenities to hotel")
    void addAmenitiesToHotel_WithValidAmenities_AddsSuccessfully() {
        Set<String> newAmenities = new HashSet<>(Arrays.asList("Pool", "Spa"));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        hotelService.addAmenitiesToHotel(1L, newAmenities);

        assertThat(testHotel.getAmenities()).contains("WiFi", "Parking", "Pool", "Spa");
        verify(hotelRepository).save(testHotel);
    }

    @Test
    @DisplayName("Should throw exception when adding null amenities")
    void addAmenitiesToHotel_WithNullAmenities_ThrowsException() {
        assertThatThrownBy(() -> hotelService.addAmenitiesToHotel(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amenities list cannot be null or empty");
    }

    @Test
    @DisplayName("Should return histogram by brand")
    void getHistogram_ByBrand_ReturnsCorrectCount() {
        Hotel hotel2 = new Hotel();
        hotel2.setBrand("Hilton");
        Hotel hotel3 = new Hotel();
        hotel3.setBrand("Marriott");

        when(hotelRepository.findAll()).thenReturn(Arrays.asList(testHotel, hotel2, hotel3));

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertThat(result).hasSize(2);
        assertThat(result.get("Hilton")).isEqualTo(2L);
        assertThat(result.get("Marriott")).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return histogram by city")
    void getHistogram_ByCity_ReturnsCorrectCount() {
        Hotel hotel2 = new Hotel();
        hotel2.setAddress(testAddress);

        Hotel hotel3 = new Hotel();
        Address address3 = new Address();
        address3.setCity("Moscow");
        hotel3.setAddress(address3);

        when(hotelRepository.findAll()).thenReturn(Arrays.asList(testHotel, hotel2, hotel3));

        // Act
        Map<String, Long> result = hotelService.getHistogram("city");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get("Minsk")).isEqualTo(2L);
        assertThat(result.get("Moscow")).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception for invalid histogram parameter")
    void getHistogram_WithInvalidParameter_ThrowsException() {
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid parameter");
    }
}