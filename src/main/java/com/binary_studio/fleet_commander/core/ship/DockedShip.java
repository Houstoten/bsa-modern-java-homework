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

	private DockedShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP, PositiveInteger powergridOutput,
			PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate, PositiveInteger speed,
			PositiveInteger size) {
		this.name = name;
		this.shieldHP = shieldHP;
		this.hullHP = hullHP;
		this.powergridOutput = powergridOutput;
		this.capacitorAmount = capacitorAmount;
		this.capacitorRechargeRate = capacitorRechargeRate;
		this.speed = speed;
		this.size = size;
	}

	public static DockedShip construct(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
			PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
			PositiveInteger speed, PositiveInteger size) {

		if (name == null || name.trim().equals("")) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}

		return new DockedShip(name, shieldHP, hullHP, powergridOutput, capacitorAmount, capacitorRechargeRate, speed,
				size);
	}

	@Override
	public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {
		if (subsystem == null && this.attackSubsystem != null) {
			this.powergridOutput = PositiveInteger
					.of(this.powergridOutput.value() + this.attackSubsystem.getPowerGridConsumption().value());
			this.attackSubsystem = null;
			return;
		}
		if (this.powergridOutput.value() >= subsystem.getPowerGridConsumption().value()) {
			this.powergridOutput = PositiveInteger
					.of(this.powergridOutput.value() - subsystem.getPowerGridConsumption().value());
			this.attackSubsystem = subsystem;
		}
		else {
			throw new InsufficientPowergridException(
					subsystem.getPowerGridConsumption().value() - this.powergridOutput.value());
		}

	}

	@Override
	public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {
		if (subsystem == null && this.defenciveSubsystem != null) {
			this.powergridOutput = PositiveInteger
					.of(this.powergridOutput.value() + this.defenciveSubsystem.getPowerGridConsumption().value());
			this.defenciveSubsystem = null;
			return;
		}
		if (this.powergridOutput.value() >= subsystem.getPowerGridConsumption().value()) {
			this.powergridOutput = PositiveInteger
					.of(this.powergridOutput.value() - subsystem.getPowerGridConsumption().value());
			this.defenciveSubsystem = subsystem;
		}
		else {
			throw new InsufficientPowergridException(
					subsystem.getPowerGridConsumption().value() - this.powergridOutput.value());
		}

	}

	public CombatReadyShip undock() throws NotAllSubsystemsFitted {
		if ((this.attackSubsystem == null && this.defenciveSubsystem == null)) {
			throw NotAllSubsystemsFitted.bothMissing();
		}
		else {
			if ((this.attackSubsystem == null)) {
				throw NotAllSubsystemsFitted.attackMissing();
			}
			else {
				if (this.defenciveSubsystem == null) {
					throw NotAllSubsystemsFitted.defenciveMissing();
				}
			}
		}
		this.shipWrapper = new ShipWrapper(this, this.name, this.shieldHP, this.hullHP, this.capacitorAmount,
				this.capacitorRechargeRate, this.speed, this.size, this.attackSubsystem, this.defenciveSubsystem);
		return this.shipWrapper.getCombatReady();
	}

}
