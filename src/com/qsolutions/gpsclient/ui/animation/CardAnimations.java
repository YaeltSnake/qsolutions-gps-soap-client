package com.qsolutions.gpsclient.ui.animation;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * Utility class providing reusable animations for fleet-tracker UI components.
 *
 * <p>All methods are static.  Callers own the returned {@link Timeline}
 * instances and are responsible for stopping them when the owning node is
 * removed from the scene graph.</p>
 */
public final class CardAnimations {

    private CardAnimations() {}

    /**
     * Fades {@code node} from fully transparent to fully opaque.
     *
     * <p>Sets {@code node} opacity to {@code 0} before starting so the
     * transition is always consistent regardless of prior state.</p>
     *
     * @param node       the node to animate; must not be {@code null}
     * @param durationMs total fade duration in milliseconds
     */
    public static void fadeIn(Node node, double durationMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Fades {@code node} from fully transparent to fully opaque, starting
     * after an initial delay.
     *
     * <p>Useful for cascading entrance animations where each item in a list
     * appears slightly after the previous one.</p>
     *
     * @param node       the node to animate; must not be {@code null}
     * @param durationMs total fade duration in milliseconds
     * @param delayMs    delay before the fade begins, in milliseconds
     */
    public static void fadeInWithDelay(Node node, double durationMs, double delayMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.play();
    }

    /**
     * Creates a looping countdown {@link Timeline} that drives a label and
     * progress bar for the "next pulse" indicator on a unit card.
     *
     * <p>Every second the counter decrements by one.  When it reaches zero the
     * label shows {@code "ahora"} for one tick, then the cycle resets to
     * {@code totalSeconds} and starts again — providing a continuous visual
     * loop without stopping the timeline.</p>
     *
     * <p>The caller is responsible for calling {@link Timeline#play()} and,
     * when the owning card is disposed, {@link Timeline#stop()}.</p>
     *
     * <pre>
     *   Timeline t = CardAnimations.createCountdownTimer(lblSeg, bar, 900);
     *   t.play();
     * </pre>
     *
     * @param labelSegundos label that shows the remaining time text
     * @param progressBar   bar whose progress reflects remaining fraction
     * @param totalSeconds  full cycle length in seconds (e.g. {@code 900} for 15 min)
     * @return a configured but not-yet-playing {@link Timeline}
     */
    public static Timeline createCountdownTimer(Label labelSegundos,
                                                ProgressBar progressBar,
                                                int totalSeconds) {
        // int[] used as a mutable counter inside the lambda closure
        int[] counter = {totalSeconds};

        KeyFrame frame = new KeyFrame(Duration.seconds(1), e -> {
            counter[0]--;
            if (counter[0] <= 0) {
                labelSegundos.setText("ahora");
                progressBar.setProgress(0);
                counter[0] = totalSeconds;
            } else {
                labelSegundos.setText("en " + counter[0] + " seg");
                progressBar.setProgress((double) counter[0] / totalSeconds);
            }
        });

        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }
}
