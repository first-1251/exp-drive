package org.team1251.frc.robot;

public class Util {

    /**
     * Applies a curve to an input value (as seen here: https://www.desmos.com/calculator/scr5agfmia).
     *
     * @param value The raw input value.
     * @param dampeningStrength A value between 0 and 1. A lower value applies less dampening to low input values.
     * @param dampeningRangeFactor Dampening applies heavier to lower input values. A higher dampeningRangeFactor causes
     *                             heavier dampening to apply to a wider range of input values. At a value of 1 the
     *                             range is empty thus no dampening occurs. With a maximum dampeningStrength (1):
     *                                - A dampeningRangeFactor of 2 reduces an input of .05 to about .25
     *                                - A dampeningRangeFactor of 3 reduces an input of .05 to about .125
     *                                - A dampeningRangeFactor of 9 reduces an input of .05 to below .002
     */
    public static double applyInputCurve(double value, double dampeningStrength, int dampeningRangeFactor) {
        // Do the math, preserve the original sign.
        //
        double refined = (dampeningStrength * Math.pow(value, dampeningRangeFactor)) + ((1 - dampeningStrength) * value);
        return value < 0 ? -refined : refined;
    }

    public static class ValueSmoother {
        /**
         * A record of past values used to smooth out the latest value.
         */
        private double[] smoothing;

        /**
         * If true, then a change in sign between two contiguous samples will reset
         */
        private final boolean resetOnSignChange;

        /**
         * Convenience copy of the last received value. Saves the effort of digging it out of the
         * smoothing array.
         */
        private double lastRawValue;

        /**
         * Convenience copy of the last smoothed value. Saves the effort of repeating the sample math.
         */
        private double lastSmoothedValue;

        /**
         * The number of past values to use to smooth out the current value.
         */
        private final int numSamples;

        /**
         *
         * @param numSamples The number of samples to apply
         * @param resetOnSignChange If true, all past samples will be discarded if the value has changed sign since
         *                          the last sample.
         */
        public ValueSmoother(int numSamples, boolean resetOnSignChange) {
            this.numSamples = numSamples;
            this.smoothing = new double[numSamples];
            this.resetOnSignChange = resetOnSignChange;
        }

        /**
         * Takes in a new value and returns the resulting smoothed value.
         */
        public double getSmoothedValue(double newValue) {
            return this.applySmoothing(newValue);
        }

        /**
         * Applies smoothing to the current stick value, adding the current value to the history of recent reads.
         *
         * @param currentValue The current stick reading.
         *
         * @return The current stick value smoothed over past readings.
         */
        private double applySmoothing(double currentValue) {

            if (resetOnSignChange && hasSignChanged(currentValue)) {
                // Re-initialize the sample list.
                smoothing = new double[numSamples];
            }

            double sum = 0.0;
            for (int i = 0; i < numSamples; i++) {
                // See if this is the last iteration.
                if (i < numSamples - 1) {
                    // Replace this value with the one after it. If this the first iteration, the existing
                    // value at this index is discarded. All others are preserved by the shifting that occurred
                    // in the previous iteration.
                    smoothing[i] = smoothing[i + 1];
                } else {
                    // We've reached the last iteration. Nothing more to shift. The new value becomes the last element
                    // (or the "newest") element of the array.
                    smoothing[i] = currentValue;
                }

                // Aggregate the value as we pass by so that we don't need to loop again.
                sum += smoothing[i];
            }

            lastRawValue = currentValue;
            return lastSmoothedValue = sum / numSamples;
        }

        private boolean hasSignChanged(double currentValue) {
            return (lastSmoothedValue < 0 && currentValue > 0) || (lastSmoothedValue > 0 && currentValue < 0);
        }
    }
}
