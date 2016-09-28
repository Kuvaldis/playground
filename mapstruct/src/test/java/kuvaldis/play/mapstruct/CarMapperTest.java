package kuvaldis.play.mapstruct;

import kuvaldis.play.mapstruct.domain.Car;
import kuvaldis.play.mapstruct.domain.CarType;
import kuvaldis.play.mapstruct.dto.CarDto;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarMapperTest {

    @Test
    public void shouldMapCarToDto() {
        // given
        final Car car = new Car("Morris", 5, CarType.SEDAN);

        // when
        final CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        // then
        assertNotNull(carDto);
        assertEquals("Morris", carDto.getMake());
        assertEquals(5, carDto.getSeatCount());
        assertEquals("SEDAN", carDto.getType());
    }
}