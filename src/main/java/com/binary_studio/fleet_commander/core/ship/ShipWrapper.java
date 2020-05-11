package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class ShipWrapper {

	private CombatReadyShip combatReadyShip;

	private DockedShip dockedShip;

	private CommonShipData data;

	public DockedShip getDocked() {
		return this.dockedShip;
	}

	public CombatReadyShip getCombatReady() {
		return this.combatReadyShip;
	}

	public CommonShipData getData() {
		return this.data;
	}

	public void getBackToTheDock() {
		this.combatReadyShip = null;
	}

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

		CommonShipData(String name, PositiveInteger shieldHP, PositiveInteger hullHP, PositiveInteger capacitorAmount,
				PositiveInteger capacitorRechargeRate, PositiveInteger speed, PositiveInteger size,
				AttackSubsystem attackSubsystem, DefenciveSubsystem defenciveSubsystem) {
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
			return this.defaultCapacitorAmount;
		}

		public void setSpeed(PositiveInteger speed) {
			this.speed = speed;
		}

		public PositiveInteger getDefaultShieldHP() {
			return this.defaultShieldHP;
		}

		public PositiveInteger getDefaultHullHP() {
			return this.defaultHullHP;
		}

		public String getName() {
			return this.name;
		}

		public PositiveInteger getShieldHP() {
			return this.shieldHP;
		}

		public PositiveInteger getHullHP() {
			return this.hullHP;
		}

		public PositiveInteger getCapacitorAmount() {
			return this.capacitorAmount;
		}

		public PositiveInteger getCapacitorRechargeRate() {
			return this.capacitorRechargeRate;
		}

		public PositiveInteger getSpeed() {
			return this.speed;
		}

		public PositiveInteger getSize() {
			return this.size;
		}

		public AttackSubsystem getAttackSubsystem() {
			return this.attackSubsystem;
		}

		public DefenciveSubsystem getDefenciveSubsystem() {
			return this.defenciveSubsystem;
		}

	}

	public ShipWrapper(DockedShip dockedShip, String name, PositiveInteger shieldHP, PositiveInteger hullHP,
			PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate, PositiveInteger speed,
			PositiveInteger size, AttackSubsystem attackSubsystem, DefenciveSubsystem defenciveSubsystem) {
		this.dockedShip = dockedShip;
		this.combatReadyShip = CombatReadyShip.construct(this);
		this.data = new CommonShipData(name, shieldHP, hullHP, capacitorAmount, capacitorRechargeRate, speed, size,
				attackSubsystem, defenciveSubsystem);
	}

}
