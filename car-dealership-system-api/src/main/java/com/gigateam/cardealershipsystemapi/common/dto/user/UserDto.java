package com.gigateam.cardealershipsystemapi.common.dto.user;

import lombok.Data;

@Data
public class UserDto {

  private Long id;
  private String name;
  private String surname;
  private String address;
  private String phoneNumber;
  private String email;

}