package kuvaldis.play.mapstruct;

import kuvaldis.play.mapstruct.domain.Car;
import kuvaldis.play.mapstruct.dto.CarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mappings({
            @Mapping(source = "numberOfSeats", target = "seatCount"),
            @Mapping(source = "price", target = "price", numberFormat = "$#.00")
    })
    CarDto carToCarDto(Car car);
}
