package com.soen.synapsis.appuser;

/**
 * Interface for framework authentication services to implement,
 * to allow for the internal AppUser they wrap to be retrieved
 * as needed.
 */
public interface AppUserAuth {

    /**
     * Retrieves AppUser wrapped by implementing services.
     * @return An instance of AppUser that may or may not be authenticated.
     */
    AppUser getAppUser();
}
