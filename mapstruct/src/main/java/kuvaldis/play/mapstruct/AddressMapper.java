package kuvaldis.play.mapstruct;

import kuvaldis.play.mapstruct.domain.Address;
import kuvaldis.play.mapstruct.domain.Person;
import kuvaldis.play.mapstruct.dto.DeliveryAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CarMapper.class)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mappings({
            @Mapping(source = "person.firstName", target = "firstName"),
            @Mapping(source = "person.lastName", target = "lastName"),
            @Mapping(source = "address.city", target = "city"),
            @Mapping(source = "address.addressLine1", target = "address"),
            @Mapping(source = "person.car", target = "carDto")
    })
    DeliveryAddressDto personAndAddressToDeliveryAddressDto(Person person, Address address);
}
