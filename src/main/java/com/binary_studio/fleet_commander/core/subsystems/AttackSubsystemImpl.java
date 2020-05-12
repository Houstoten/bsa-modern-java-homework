package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;

public final class AttackSubsystemImpl implements AttackSubsystem {

	private String name;

	private PositiveInteger powergridRequirments;

	private PositiveInteger capacitorConsumption;

	private PositiveInteger optimalSpeed;

	private PositiveInteger optimalSize;

	private PositiveInteger baseDamage;

	private AttackSubsystemImpl(String name, PositiveInteger powergridRequirments, PositiveInteger capacitorConsumption,
			PositiveInteger optimalSpeed, PositiveInteger optimalSize, PositiveInteger baseDamage) {
		this.name = name;
		this.powergridRequirments = powergridRequirments;
		this.capacitorConsumption = capacitorConsumption;
		this.optimalSpeed = optimalSpeed;
		this.optimalSize = optimalSize;
		this.baseDamage = baseDamage;
	}

	public static AttackSubsystemImpl construct(String name, PositiveInteger powergridRequirments,
			PositiveInteger capacitorConsumption, PositiveInteger optimalSpeed, PositiveInteger optimalSize,
			PositiveInteger baseDamage) throws IllegalArgumentException {
		if (name == null || name.trim().equals("")) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}
		return new AttackSubsystemImpl(name, powergridRequirments, capacitorConsumption, optimalSpeed, optimalSize,
				baseDamage);
	}

	@Override
	public PositiveInteger getPowerGridConsumption() {
		return this.powergridRequirments;
	}

	@Override
	public PositiveInteger getCapacitorConsumption() {
		return this.capacitorConsumption;
	}

	@Override
	public PositiveInteger attack(Attackable target) {
		var sizeReductionModifier = (target.getSize().compareTo(this.optimalSize) >= 0) ? 1
				: target.getSize().value() / (double) this.optimalSize.value();

		var speedReductionModifier = (target.getCurrentSpeed().compareTo(this.optimalSpeed) <= 0) ? 1
				: this.optimalSize.value() / (double) (2 * target.getSize().value());
		return PositiveInteger
				.of((int) Math.ceil(this.baseDamage.value() * Math.min(sizeReductionModifier, speedReductionModifier)));
	}

	@Override
	public String getName() {
		return this.name;
	}

}
