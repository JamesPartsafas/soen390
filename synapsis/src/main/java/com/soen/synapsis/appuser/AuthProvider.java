package com.soen.synapsis.appuser;

/**
 * Used to mark how a user was registered, either through Synapsis'
 * local registration system or through Google SSO.
 */
public enum AuthProvider {
    LOCAL,
    GOOGLE
}
