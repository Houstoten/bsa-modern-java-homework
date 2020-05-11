package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class ShipWrapper {
    private CombatReadyShip combatReadyShip;
    private DockedShip dockedShip;
    private CommonShipData data;

    class CommonShipData {
        private String name;
        private PositiveInteger shieldHP;
        private PositiveInteger hullHP;
        private PositiveInteger capacitorAmount;
        private PositiveInteger capacitorRechargeRate;
        private PositiveInteger speed;
        private PositiveInteger size;
        private AttackSubsystem attackSubsystem;
        private DefenciveSubsystem defenciveSubsystem;
        private PositiveInteger defaultShieldHP;
        private PositiveInteger defaultHullHP;
        private PositiveInteger defaultCapacitorAmount;


        public CommonShipData(String name, PositiveInteger shieldHP, PositiveInteger hullHP
                , PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate
                , PositiveInteger speed, PositiveInteger size, AttackSubsystem attackSubsystem
                , DefenciveSubsystem defenciveSubsystem) {
            this.name = name;
            this.shieldHP = shieldHP;
            this.hullHP = hullHP;
            this.defaultHullHP = hullHP;
            this.defaultShieldHP = shieldHP;
            this.capacitorAmount = capacitorAmount;
            this.defaultCapacitorAmount = capacitorAmount;
            this.capacitorRechargeRate = capacitorRechargeRate;
            this.speed = speed;
            this.size = size;
            this.attackSubsystem = attackSubsystem;
            this.defenciveSubsystem = defenciveSubsystem;
        }

        public void setShieldHP(PositiveInteger shieldHP) {
            this.shieldHP = shieldHP;
        }

        public void setHullHP(PositiveInteger hullHP) {
            this.hullHP = hullHP;
        }

        public void setCapacitorAmount(PositiveInteger capacitorAmount) {
            this.capacitorAmount = capacitorAmount;
        }

        public PositiveInteger getDefaultCapacitorAmount() {
            return defaultCapacitorAmount;
        }

        public void setSpeed(PositiveInteger speed) {
            this.speed = speed;
        }

        public PositiveInteger getDefaultShieldHP() {
            return defaultShieldHP;
        }

        public PositiveInteger getDefaultHullHP() {
            return defaultHullHP;
        }

        public String getName() {
            return name;
        }

        public PositiveInteger getShieldHP() {
            return shieldHP;
        }

        public PositiveInteger getHullHP() {
            return hullHP;
        }

        public PositiveInteger getCapacitorAmount() {
            return capacitorAmount;
        }

        public PositiveInteger getCapacitorRechargeRate() {
            return capacitorRechargeRate;
        }

        public PositiveInteger getSpeed() {
            return speed;
        }

        public PositiveInteger getSize() {
            return size;
        }

        public AttackSubsystem getAttackSubsystem() {
            return attackSubsystem;
        }

        public DefenciveSubsystem getDefenciveSubsystem() {
            return defenciveSubsystem;
        }
    }

    public DockedShip getDocked() {
        return dockedShip;
    }

    public CombatReadyShip getCombatReady() {
        return combatReadyShip;
    }

    public CommonShipData getData() {
        return data;
    }

    public ShipWrapper(DockedShip dockedShip, String name, PositiveInteger shieldHP, PositiveInteger hullHP
            , PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate
            , PositiveInteger speed, PositiveInteger size, AttackSubsystem attackSubsystem
            , DefenciveSubsystem defenciveSubsystem) {
        this.dockedShip = dockedShip;
        this.combatReadyShip = CombatReadyShip.construct(this);
        data = new CommonShipData(name, shieldHP, hullHP, capacitorAmount, capacitorRechargeRate, speed, size
                , attackSubsystem, defenciveSubsystem);
    }

    public void getBackToTheDock() {
        combatReadyShip = null;
    }

}
