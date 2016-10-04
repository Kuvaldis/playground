package kuvaldis.play.mapstruct;

import kuvaldis.play.mapstruct.domain.*;
import kuvaldis.play.mapstruct.dto.CarDto;
import kuvaldis.play.mapstruct.dto.DeliveryAddressDto;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapStructTest {

    @Test
    public void testSimpleMapping() {
        // given
        final Car car = new Car("Morris", 5, CarType.SEDAN, 123.456);

        // when
        final CarDto dto = CarMapper.INSTANCE.carToCarDto(car);

        // then
        assertNotNull(dto);
        assertEquals("Morris", dto.getMake());
        assertEquals(5, dto.getSeatCount());
        assertEquals("SEDAN", dto.getType());
        assertEquals("$123.46", dto.getPrice());
    }

    @Test
    public void testSeveralObjectsToOneMapping() throws Exception {
        // given
        final Car car = new Car("Morris", 5, CarType.SEDAN, 123.456);
        final Person person = new Person("Willy", "Trombone", car);
        final Address address = new Address("Whatever", "Dude");

        // when
        final DeliveryAddressDto dto = AddressMapper.INSTANCE.personAndAddressToDeliveryAddressDto(person, address);

        // then
        assertNotNull(dto);
        assertEquals("Willy", dto.getFirstName());
        assertEquals("Trombone", dto.getLastName());
        assertEquals("Whatever", dto.getCity());
        assertEquals("Dude", dto.getAddress());

        final CarDto carDto = dto.getCarDto();
        assertNotNull(carDto);
        assertEquals("Morris", carDto.getMake());
        assertEquals(5, carDto.getSeatCount());
        assertEquals("SEDAN", carDto.getType());
        assertEquals("$123.46", carDto.getPrice());
    }

    @Test
    public void testQualifiers() throws Exception {
        // given
        final EnglishMovieRelease englishMovieRelease = new EnglishMovieRelease("title");

        // when
        final RussianMovieRelease russianMovieRelease = MovieMapper.INSTANCE.toRussian(englishMovieRelease);

        assertEquals("In Russian: title", russianMovieRelease.getTitle());

    }
}