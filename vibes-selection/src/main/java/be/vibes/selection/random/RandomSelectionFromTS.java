package be.vibes.selection.random;

import be.vibes.ts.TransitionSystem;

public class RandomSelectionFromTS extends RandomSelection<TransitionSystem> {

    public RandomSelectionFromTS(TransitionSystem transitionSystem, int maxNbrTry, int maxLength) {
        super(transitionSystem, maxNbrTry, maxLength);
    }

    public RandomSelectionFromTS(TransitionSystem transitionSystem) {
        super(transitionSystem);
    }

    public RandomSelectionFromTS(TransitionSystem transitionSystem, int maxLength) {
        super(transitionSystem, maxLength);
    }

}
