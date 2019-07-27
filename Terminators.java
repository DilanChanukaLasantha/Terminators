
import robocode.*;

//import robocode.Robot;
import robocode.ScannedRobotEvent;
//import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

/**
 *
 * @author SICT Computers
 */
public class Terminators extends CharlieBot{
	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move

	/**
	 * run: Move around the walls
	 */
	public void run() {
		// Set colors
		setBodyColor(Color.red);
		setGunColor(Color.yellow);
		setRadarColor(Color.red);
		setBulletColor(Color.cyan);
		setScanColor(Color.cyan);

		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunLeft(90);
		turnLeft(90);

		while (true) {
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnLeft(90);
		}
	}

	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		turnLeft(90);
		turnGunLeft(180);
		
		fire(50);
		
		// If he's in front of us, set back up a bit.
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(1000);
		} // else he's in back of us, so set ahead a bit.
		else {
			ahead(1000);
		}
	}
	public void onHitByBullet(HitByBulletEvent e) {
		
		// Replace the next line with any behavior you would like
		//back(10);
		//fire(50);
		turnLeft(90);
		turnGunLeft(180);
		
		fire(50);
		
		
		back(1000);
		
		if (getGunHeat() == 0) {
				turnGunLeft(180);
		
		
			fire(Rules.MAX_BULLET_POWER);
		}
		
		turnGunLeft(90);
		fire(50);
		
	}
	/**
	 * onScannedRobot:  Fire!
	 */
	public void onRoboDetected(ScannedRobotEvent e) {
		turnGunLeft(180);
		
		fire(50);
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
					turnGunLeft(180);
		
					fire(50);
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}
		turnLeft(90);

		// Note that scan is called automatically when the robot is moving.
		// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
		// wall, so that we do not start moving up it until it's gone.
		if (peek) {
			scan();
		}
	}

}
    

