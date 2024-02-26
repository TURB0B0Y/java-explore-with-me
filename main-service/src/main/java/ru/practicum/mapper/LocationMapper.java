package ru.practicum.mapper;

import ru.practicum.dto.location.CreateLocationDTO;
import ru.practicum.dto.location.LocationDTO;
import ru.practicum.model.Location;

public class LocationMapper {

    public static LocationDTO toModel(Location location) {
        if (location == null)
            return null;
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setLon(location.getLon());
        dto.setLat(location.getLat());
        return dto;
    }

    public static Location fromDto(CreateLocationDTO dto) {
        if (dto == null)
            return null;
        Location location = new Location();
        location.setLon(dto.getLon());
        location.setLat(dto.getLat());
        return location;
    }

}
