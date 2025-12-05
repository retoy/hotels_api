package retoy.hotels_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Address {

    @NotNull(message = "House number is required")
    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @NotBlank(message = "Street is required")
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Country is required")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Post code is required")
    @Column(name = "post_code", nullable = false)
    private String postCode;
}
