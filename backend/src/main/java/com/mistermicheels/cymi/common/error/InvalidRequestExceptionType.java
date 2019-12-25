package com.mistermicheels.cymi.common.error;

/**
 * Can be used to give InvalidRequestException instances a machine-readable
 * type. These types can be used to trigger behavior in response to specific
 * errors without having to depend on error messages.
 * 
 * Be cautious when changing the names of these types, as parts of the system
 * other than this backend may depend on them.
 */
public enum InvalidRequestExceptionType {

    UserNotSignedUp, EmailNotConfirmed, InvalidPassword, EmailAlreadyTaken, UserAlreadyInvited,
    UserAlreadyMember

}
