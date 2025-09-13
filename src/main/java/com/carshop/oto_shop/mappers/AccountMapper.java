package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.account.AccountRequest;
import com.carshop.oto_shop.entities.Account;
import org.mapstruct.*;

@Mapper (componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountRequest accountRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateAccount(AccountRequest request, @MappingTarget Account account);
}
