package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
}
