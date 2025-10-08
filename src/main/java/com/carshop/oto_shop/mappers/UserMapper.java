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

    // Map both User fields and Account fields (1:1 relationship)
    @Mapping(target = "avatarUrl", ignore = false)
    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.username", target = "username")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.role", target = "role")
    @Mapping(source = "account.status", target = "status")
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "avatarUrl", ignore = true)
    void updateUser(UserRequest userRequest, @MappingTarget User user);
}
