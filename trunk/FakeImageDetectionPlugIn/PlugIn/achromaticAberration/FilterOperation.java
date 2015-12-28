/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package achromaticAberration;

/**
 *
 * @author Ruj
 */
public interface FilterOperation {
    // Perform action on source and store result in the provided result
    public void action(float[] src, float[] result);
}
