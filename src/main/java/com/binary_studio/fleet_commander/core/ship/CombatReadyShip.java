package com.binary_studio.fleet_commander.core.ship;

import java.util.Optional;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.AttackResult;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.ship.contract.CombatReadyVessel;

public final class CombatReadyShip implements CombatReadyVessel {

	private ShipWrapper shipWrapper;

	private CombatReadyShip(ShipWrapper shipWrapper) {
		this.shipWrapper = shipWrapper;
	}

	public static CombatReadyShip construct(ShipWrapper shipWrapper) {
		return new CombatReadyShip(shipWrapper);
	}

	@Override
	public void endTurn() {
		if (this.shipWrapper.getData().getCapacitorAmount().value() + this.shipWrapper.getData()
				.getCapacitorRechargeRate().value() < this.shipWrapper.getData().getDefaultCapacitorAmount().value()) {
			this.shipWrapper.getData()
					.setCapacitorAmount(PositiveInteger.of(this.shipWrapper.getData().getCapacitorAmount().value()
							+ this.shipWrapper.getData().getCapacitorRechargeRate().value()));
		}
		else {
			this.shipWrapper.getData().setCapacitorAmount(this.shipWrapper.getData().getDefaultCapacitorAmount());
		}
	}

	@Override
	public void startTurn() {
	}

	@Override
	public String getName() {
		return this.shipWrapper.getData().getName();
	}

	@Override
	public PositiveInteger getSize() {
		return this.shipWrapper.getData().getSize();
	}

	@Override
	public PositiveInteger getCurrentSpeed() {
		return this.shipWrapper.getData().getSpeed();
	}

	@Override
	public Optional<AttackAction> attack(Attackable target) {
		if (this.shipWrapper.getData().getAttackSubsystem().getCapacitorConsumption()
				.compareTo(this.shipWrapper.getData().getCapacitorAmount()) <= 0) {
			this.shipWrapper.getData()
					.setCapacitorAmount(PositiveInteger.of(this.shipWrapper.getData().getCapacitorAmount().value()
							- this.shipWrapper.getData().getAttackSubsystem().getCapacitorConsumption().value()));
			return Optional.of(new AttackAction(this.shipWrapper.getData().getAttackSubsystem().attack(target), this,
					target, this.shipWrapper.getData().getAttackSubsystem()));
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public AttackResult applyAttack(AttackAction attack) {
		return ((this.shipWrapper.getData().getHullHP().value() + this.shipWrapper.getData().getShieldHP()
				.value()) <= this.shipWrapper.getData().getDefenciveSubsystem().reduceDamage(attack).damage.value())
						? (new AttackResult.Destroyed())
						: (new AttackResult.DamageRecived(attack.weapon, cascadeHPreduce(
								this.shipWrapper.getData().getDefenciveSubsystem().reduceDamage(attack).damage.value()),
								this));
	}

	@Override
	public Optional<RegenerateAction> regenerate() {
		if (this.shipWrapper.getData().getCapacitorAmount()
				.compareTo(this.shipWrapper.getData().getDefenciveSubsystem().getCapacitorConsumption()) >= 0) {

			Optional<RegenerateAction> regenerateAction = Optional
					.of(this.shipWrapper.getData().getDefenciveSubsystem().regenerate());

			if (regenerateAction.get().shieldHPRegenerated
					.value() > (this.shipWrapper.getData().getDefaultShieldHP().value()
							- this.shipWrapper.getData().getShieldHP().value())
					|| regenerateAction.get().hullHPRegenerated
							.value() > (this.shipWrapper.getData().getDefaultHullHP().value()
									- this.shipWrapper.getData().getHullHP().value())) {

				regenerateAction = Optional.of(new RegenerateAction(regenerateAction.get(),
						PositiveInteger.of(this.shipWrapper.getData().getDefaultShieldHP().value()
								- this.shipWrapper.getData().getShieldHP().value()),
						PositiveInteger.of(this.shipWrapper.getData().getDefaultHullHP().value()
								- this.shipWrapper.getData().getHullHP().value())));
			}

			this.shipWrapper.getData()
					.setCapacitorAmount(PositiveInteger.of(this.shipWrapper.getData().getCapacitorAmount().value()
							- this.shipWrapper.getData().getDefenciveSubsystem().getCapacitorConsumption().value()));

			this.shipWrapper.getData()
					.setHullHP(PositiveInteger.of(regenerateAction.get().hullHPRegenerated.value()
							+ this.shipWrapper.getData().getHullHP().value() > this.shipWrapper.getData()
									.getDefaultHullHP().value() ? this.shipWrapper.getData().getDefaultHullHP().value()
											: regenerateAction.get().hullHPRegenerated.value()
													+ this.shipWrapper.getData().getHullHP().value()));

			this.shipWrapper.getData().setShieldHP(PositiveInteger.of(regenerateAction.get().shieldHPRegenerated.value()
					+ this.shipWrapper.getData().getShieldHP().value() > this.shipWrapper.getData().getDefaultShieldHP()
							.value() ? this.shipWrapper.getData().getDefaultShieldHP().value()
									: regenerateAction.get().shieldHPRegenerated.value()
											+ this.shipWrapper.getData().getShieldHP().value()));

			return regenerateAction;
		}
		else {
			return Optional.empty();
		}
	}

	private PositiveInteger cascadeHPreduce(int damage) {
		if (damage <= this.shipWrapper.getData().getShieldHP().value()) {
			this.shipWrapper.getData()
					.setShieldHP(PositiveInteger.of(this.shipWrapper.getData().getShieldHP().value() - damage));
		}
		else {
			this.shipWrapper.getData().setHullHP(PositiveInteger.of(this.shipWrapper.getData().getHullHP().value()
					- (damage - this.shipWrapper.getData().getShieldHP().value())));
			this.shipWrapper.getData().setShieldHP(PositiveInteger.of(0));
		}
		return PositiveInteger.of(damage);
	}

}
