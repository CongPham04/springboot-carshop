package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.user.UserResponse;
import com.carshop.oto_shop.dto.user.UserRequest;
import com.carshop.oto_shop.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "userId", ignore = true)
    User toUser(UserRequest userRequest);
    @Mapping(target = "avatarUrl", ignore = false)
    UserResponse toUserResponse(User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "avatarUrl", ignore = true)
    void updateUser(UserRequest userRequest, @MappingTarget User user);
}
