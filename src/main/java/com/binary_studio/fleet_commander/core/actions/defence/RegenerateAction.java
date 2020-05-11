package com.binary_studio.fleet_commander.core.actions.defence;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;

public final class RegenerateAction {

    public final PositiveInteger shieldHPRegenerated;

    public final PositiveInteger hullHPRegenerated;

    public RegenerateAction(PositiveInteger shieldRegenerated, PositiveInteger hullRegenerated) {
        this.shieldHPRegenerated = shieldRegenerated;
        this.hullHPRegenerated = hullRegenerated;
    }

    public RegenerateAction(RegenerateAction regenerateAction
            , PositiveInteger initialShieldHP, PositiveInteger initialHullHP) {
        this(PositiveInteger.of(initialShieldHP.value() < regenerateAction.shieldHPRegenerated.value()
                        ? initialShieldHP.value()
                        : regenerateAction.shieldHPRegenerated.value())
                , PositiveInteger.of(initialHullHP.value() < regenerateAction.hullHPRegenerated.value()
                        ? initialHullHP.value()
                        : regenerateAction.hullHPRegenerated.value()));
    }

}
