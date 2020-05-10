package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DefenciveSubsystemImpl implements DefenciveSubsystem {

    private String name;
    private PositiveInteger powergridRequirments;
    private PositiveInteger capacitorConsumption;
    private PositiveInteger impactReductionPercent;
    private PositiveInteger shieldRegeneration;
    private PositiveInteger hullRegeneration;

    private DefenciveSubsystemImpl(String name, PositiveInteger powergridRequirments
            , PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent, PositiveInteger shieldRegeneration
            , PositiveInteger hullRegeneration) {
        this.name = name;
        this.powergridRequirments = powergridRequirments;
        this.capacitorConsumption = capacitorConsumption;
        this.impactReductionPercent = impactReductionPercent.value() < 95
                ? (impactReductionPercent.value() > 0
                ? impactReductionPercent
                : PositiveInteger.of(0))
                : PositiveInteger.of(95);
        this.shieldRegeneration = shieldRegeneration;
        this.hullRegeneration = hullRegeneration;
    }

    public static DefenciveSubsystemImpl construct(String name, PositiveInteger powergridConsumption
            , PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent
            , PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) throws IllegalArgumentException {
        if (name == null || name.trim().equals(""))
            throw new IllegalArgumentException("Name should be not null and not empty");
        return new DefenciveSubsystemImpl(name, powergridConsumption, capacitorConsumption, impactReductionPercent
                , shieldRegeneration, hullRegeneration);
    }

    @Override
    public PositiveInteger getPowerGridConsumption() {
        return powergridRequirments;
    }

    @Override
    public PositiveInteger getCapacitorConsumption() {
        return capacitorConsumption;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AttackAction reduceDamage(AttackAction incomingDamage) {
        return new AttackAction(PositiveInteger
                .of((int) Math.ceil(incomingDamage.damage.value() - incomingDamage.damage.value()
                        * (double) (impactReductionPercent.value()) / 100))
                , incomingDamage.attacker, incomingDamage.target, incomingDamage.weapon);
    }

    @Override
    public RegenerateAction regenerate() {
        return new RegenerateAction(shieldRegeneration, hullRegeneration);
    }

}
