package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.exceptions.InsufficientPowergridException;
import com.binary_studio.fleet_commander.core.exceptions.NotAllSubsystemsFitted;
import com.binary_studio.fleet_commander.core.ship.contract.ModularVessel;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DockedShip implements ModularVessel {
    private String name;
    private PositiveInteger shieldHP;
    private PositiveInteger hullHP;
    private PositiveInteger powergridOutput;
    private PositiveInteger capacitorAmount;
    private PositiveInteger capacitorRechargeRate;
    private PositiveInteger speed;
    private PositiveInteger size;
    private AttackSubsystem attackSubsystem;
    private DefenciveSubsystem defenciveSubsystem;
    private ShipWrapper shipWrapper;


    private DockedShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP
            , PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate
            , PositiveInteger speed, PositiveInteger size) {
        this.name = name;
        this.shieldHP = shieldHP;
        this.hullHP = hullHP;
        this.powergridOutput = powergridOutput;
        this.capacitorAmount = capacitorAmount;
        this.capacitorRechargeRate = capacitorRechargeRate;
        this.speed = speed;
        this.size = size;
    }

    public static DockedShip construct(String name, PositiveInteger shieldHP, PositiveInteger hullHP
            , PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate
            , PositiveInteger speed, PositiveInteger size) {

        if (name == null || name.trim().equals(""))
            throw new IllegalArgumentException("Name should be not null and not empty");

        return new DockedShip(name, shieldHP, hullHP, powergridOutput, capacitorAmount
                , capacitorRechargeRate, speed, size);
    }

    @Override
    public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {
        if (subsystem == null && attackSubsystem != null) {
            powergridOutput = PositiveInteger.of(powergridOutput.value()
                    + attackSubsystem.getPowerGridConsumption().value());
            attackSubsystem = null;
            return;
        }
        if (powergridOutput.value() >= subsystem.getPowerGridConsumption().value()) {
            powergridOutput = PositiveInteger.of(powergridOutput.value()
                    - subsystem.getPowerGridConsumption().value());
            attackSubsystem = subsystem;
        } else {
            throw new InsufficientPowergridException(subsystem.getPowerGridConsumption().value()
                    - powergridOutput.value());
        }

    }

    @Override
    public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {
        if (subsystem == null && defenciveSubsystem != null) {
            powergridOutput = PositiveInteger.of(powergridOutput.value()
                    + defenciveSubsystem.getPowerGridConsumption().value());
            defenciveSubsystem = null;
            return;
        }
        if (powergridOutput.value() >= subsystem.getPowerGridConsumption().value()) {
            powergridOutput = PositiveInteger.of(powergridOutput.value()
                    - subsystem.getPowerGridConsumption().value());
            defenciveSubsystem = subsystem;
        } else {
            throw new InsufficientPowergridException(subsystem.getPowerGridConsumption().value()
                    - powergridOutput.value());
        }

    }

    public CombatReadyShip undock() throws NotAllSubsystemsFitted {
        if ((attackSubsystem == null && defenciveSubsystem == null)) {
            throw NotAllSubsystemsFitted.bothMissing();
        } else {
            if ((attackSubsystem == null)) {
                throw NotAllSubsystemsFitted.attackMissing();
            } else {
                if (defenciveSubsystem == null) {
                    throw NotAllSubsystemsFitted.defenciveMissing();
                }
            }
        }
        shipWrapper = new ShipWrapper(this, name, shieldHP, hullHP, capacitorAmount
                , capacitorRechargeRate, speed, size, attackSubsystem, defenciveSubsystem);
        return shipWrapper.getCombatReady();
    }

}
