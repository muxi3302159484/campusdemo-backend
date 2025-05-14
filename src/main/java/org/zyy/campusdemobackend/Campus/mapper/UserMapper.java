package org.zyy.campusdemobackend.Campus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zyy.campusdemobackend.Campus.model.User;

@Mapper
public interface UserMapper {
    User getUserById(Long id);
}