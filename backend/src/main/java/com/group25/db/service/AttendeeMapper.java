package com.group25.db.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.group25.db.model.Attendee;

@Mapper
public interface AttendeeMapper {

    void insertUser(Attendee attendee);

    Attendee selectUserByEmail(@Param("userId") String userId);
}

