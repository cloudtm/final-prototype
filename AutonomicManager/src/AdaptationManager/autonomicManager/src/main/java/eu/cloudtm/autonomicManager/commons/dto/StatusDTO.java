package eu.cloudtm.autonomicManager.commons.dto;

import eu.cloudtm.autonomicManager.commons.PlatformConfiguration;
import eu.cloudtm.autonomicManager.commons.PlatformState;
import eu.cloudtm.autonomicManager.commons.PlatformTuning;
import eu.cloudtm.autonomicManager.commons.State;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 8/31/13
 */
public class StatusDTO {

    private final PlatformState currentState;

    private final PlatformTuning tuning;

    private final PlatformConfiguration configuration;

    public StatusDTO(State state, PlatformTuning tuning, PlatformConfiguration configuration){
        this.currentState = state.current();
        this.tuning = tuning;
        this.configuration = configuration;
    }

}
