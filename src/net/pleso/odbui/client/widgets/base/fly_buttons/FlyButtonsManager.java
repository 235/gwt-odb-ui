package net.pleso.odbui.client.widgets.base.fly_buttons;


public class FlyButtonsManager {

	private static IFlyButtonsHolder currentFlyButtonsHolder = null;
	
	private static FlyButtonsHideTimer flyButtonsHideTimer = new FlyButtonsHideTimer();
	
	public static void showOwnFlyButtons(IFlyButtonsHolder flyButtonsHolder) {
		if (!FlyButtonsManager.checkBeforeShow(flyButtonsHolder))
			return;
		
		if (!FlyButtonsManager.tryHideFlyButtonsBy(flyButtonsHolder))
			return;
		
		flyButtonsHolder.showFlyButtons();
	}
	
	private static boolean tryHideFlyButtonsBy(IFlyButtonsHolder flyButtonsHolder) {
		if (currentFlyButtonsHolder != null) {
			if (!FlyButtonsManager.hideFlyButtonsBy(flyButtonsHolder))
				return false;
		} else
			currentFlyButtonsHolder = flyButtonsHolder;
		
		return true;
	}
	
	public static boolean anyFlyButtonsShown() {
		return currentFlyButtonsHolder != null;
	}
	
	public static boolean thisFlyButtonsHolderShown(IFlyButtonsHolder flyButtonsHolder) {
		return flyButtonsHolder == currentFlyButtonsHolder;
	}
	
	public static void timedHideOwnFlyButtons(IFlyButtonsHolder flyButtonsHolder) {
		if (flyButtonsHolder == currentFlyButtonsHolder)
			flyButtonsHideTimer.schedule();
	}
	
	private static boolean checkBeforeShow(IFlyButtonsHolder flyButtonsHolder) {
		if (flyButtonsHolder == currentFlyButtonsHolder) {
			flyButtonsHideTimer.cancel();
			return false;
		} else
			return true;
	}
	
	public static boolean hideFlyButtons() {
		return hideFlyButtonsBy(null);
	}
	
	private static boolean hideFlyButtonsBy(IFlyButtonsHolder flyButtonsHolder) {
		if (currentFlyButtonsHolder != null)
			if (currentFlyButtonsHolder.hideFlyButtons()) {
				flyButtonsHideTimer.cancel();
				currentFlyButtonsHolder = flyButtonsHolder;
				return true;
			} else
				return false;
		else
			return false;
	}
}
