package retoy.hotels_api.specification;

import retoy.hotels_api.model.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.stream.Collectors;

public class HotelSpecification {
    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) ->
                brand == null ? null :
                        cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? null :
                        cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase());
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) ->
                country == null ? null :
                        cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase());
    }

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }

            List<String> lowerAmenities = amenities.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hotel> subRoot = subquery.correlate(root);
            Join<Hotel, String> amenityJoin = subRoot.join("amenities");

            subquery.select(cb.countDistinct(amenityJoin))
                    .where(cb.lower(amenityJoin).in(lowerAmenities));

            return cb.equal(subquery, (long) amenities.size());
        };
    }
}