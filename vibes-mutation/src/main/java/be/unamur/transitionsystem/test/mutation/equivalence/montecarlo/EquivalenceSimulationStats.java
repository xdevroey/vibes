/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import java.util.HashMap;

/**
 *
 * @author gperroui
 */
public class EquivalenceSimulationStats {

    public static HashMap<String, Integer> failedRuns = null;

    public static void init() {
        if (failedRuns == null) {
            failedRuns = new HashMap<>();
        }
    }

    public static synchronized void addFailedRuns(String mutant, int failed) {
        //int existing
        if (failedRuns == null) {
            init();
        }
        Integer val = failedRuns.get(mutant);
        if (val == null) {
            failedRuns.put(mutant, failed);
        } else {
            failedRuns.put(mutant, failed + val);
        }

    }

    public static synchronized int getMutantFailedRuns(String mutantName) {
        return failedRuns.get(mutantName);
    }

}
