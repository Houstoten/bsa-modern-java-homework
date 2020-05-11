package com.binary_studio.fleet_commander.core.ship;

import java.util.Optional;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.AttackResult;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.NamedEntity;
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
        if (shipWrapper.getData().getCapacitorAmount().value()
                + shipWrapper.getData().getCapacitorRechargeRate().value()
                < shipWrapper.getData().getDefaultCapacitorAmount().value()) {
            shipWrapper.getData().setCapacitorAmount(PositiveInteger
                    .of(shipWrapper.getData().getCapacitorAmount().value()
                            + shipWrapper.getData().getCapacitorRechargeRate().value()));
        } else {
            shipWrapper.getData().setCapacitorAmount(shipWrapper.getData().getDefaultCapacitorAmount());
        }
    }

    @Override
    public void startTurn() {
    }

    @Override
    public String getName() {
        return shipWrapper.getData().getName();
    }

    @Override
    public PositiveInteger getSize() {
        return shipWrapper.getData().getSize();
    }

    @Override
    public PositiveInteger getCurrentSpeed() {
        return shipWrapper.getData().getSpeed();
    }

    @Override
    public Optional<AttackAction> attack(Attackable target) {
        if (shipWrapper.getData().getAttackSubsystem().getCapacitorConsumption().value()
                <= shipWrapper.getData().getCapacitorAmount().value()) {
            shipWrapper.getData().setCapacitorAmount(PositiveInteger
                    .of(shipWrapper.getData().getCapacitorAmount().value()
                            - shipWrapper.getData()
                            .getAttackSubsystem()
                            .getCapacitorConsumption()
                            .value()));
            return Optional.of(new AttackAction(shipWrapper.getData().getAttackSubsystem().attack(target)
                    , this
                    , target
                    , shipWrapper.getData().getAttackSubsystem()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public AttackResult applyAttack(AttackAction attack) {
        return ((shipWrapper.getData().getHullHP().value() + shipWrapper.getData().getShieldHP().value())
                <= shipWrapper.getData().getDefenciveSubsystem().reduceDamage(attack).damage.value())
                ? (new AttackResult.Destroyed())
                : (new AttackResult.DamageRecived(attack.weapon
                , cascadeHPreduce(shipWrapper.getData().getDefenciveSubsystem().reduceDamage(attack).damage.value())
                , this));
    }

    @Override
    public Optional<RegenerateAction> regenerate() {
        if (shipWrapper.getData().getCapacitorAmount().value()
                >= shipWrapper.getData().getDefenciveSubsystem().getCapacitorConsumption().value()) {

            Optional<RegenerateAction> regenerateAction = Optional
                    .of(shipWrapper.getData().getDefenciveSubsystem().regenerate());

            if (regenerateAction.get().shieldHPRegenerated.value()
                    > (shipWrapper.getData().getDefaultShieldHP().value()
                    - shipWrapper.getData().getShieldHP().value())
                    || regenerateAction.get().hullHPRegenerated.value()
                    > (shipWrapper.getData().getDefaultHullHP().value()
                    - shipWrapper.getData().getHullHP().value())) {

                regenerateAction = Optional.of(new RegenerateAction(regenerateAction.get()
                        , PositiveInteger.of(shipWrapper.getData().getDefaultShieldHP().value()
                        - shipWrapper.getData().getShieldHP().value())
                        , PositiveInteger.of(shipWrapper.getData().getDefaultHullHP().value()
                        - shipWrapper.getData().getHullHP().value())));
            }

            shipWrapper.getData().setCapacitorAmount(PositiveInteger
                    .of(shipWrapper.getData().getCapacitorAmount().value()
                            - shipWrapper.getData().getDefenciveSubsystem().getCapacitorConsumption().value()));

            shipWrapper.getData().setHullHP(PositiveInteger
                    .of(regenerateAction.get().hullHPRegenerated.value()
                            + shipWrapper.getData().getHullHP().value()
                            > shipWrapper.getData().getDefaultHullHP().value()
                            ? shipWrapper.getData().getDefaultHullHP().value()
                            : regenerateAction.get().hullHPRegenerated.value()
                            + shipWrapper.getData().getHullHP().value()));

            shipWrapper.getData().setShieldHP(PositiveInteger
                    .of(regenerateAction.get().shieldHPRegenerated.value()
                            + shipWrapper.getData().getShieldHP().value()
                            > shipWrapper.getData().getDefaultShieldHP().value()
                            ? shipWrapper.getData().getDefaultShieldHP().value()
                            : regenerateAction.get().shieldHPRegenerated.value()
                            + shipWrapper.getData().getShieldHP().value()));

            return regenerateAction;
        } else {
            return Optional.empty();
        }
    }

    private PositiveInteger cascadeHPreduce(int damage) {
        if (damage <= shipWrapper.getData().getShieldHP().value()) {
            shipWrapper.getData()
                    .setShieldHP(PositiveInteger.of(shipWrapper.getData().getShieldHP().value() - damage));
        } else {
            shipWrapper.getData()
                    .setHullHP(PositiveInteger.of(shipWrapper.getData().getHullHP().value()
                            - (damage - shipWrapper.getData().getShieldHP().value())));
            shipWrapper.getData().setShieldHP(PositiveInteger.of(0));
        }
        return PositiveInteger.of(damage);
    }

}
