package kuvaldis.play.mapstruct;

import kuvaldis.play.mapstruct.domain.EnglishMovieRelease;
import kuvaldis.play.mapstruct.domain.RussianMovieRelease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = Titles.class)
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(target = "title", qualifiedByName = {"TitleTranslator", "EnglishToRussian"})
    RussianMovieRelease toRussian(EnglishMovieRelease englishMovieRelease);
}
