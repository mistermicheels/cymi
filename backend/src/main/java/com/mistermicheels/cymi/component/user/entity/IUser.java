package com.mistermicheels.cymi.component.user.entity;

import java.util.Optional;

public interface IUser {

    Long getId();

    String getEmail();

    Optional<String> getDefaultDisplayName();

}
