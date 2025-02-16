package com.ngolik.authservice.service.mapper;

import com.ngolik.authservice.dto.UserDTO;
import com.ngolik.authservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
}
